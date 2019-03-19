package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class XmlConverterFactory extends Converter.Factory {
    private XmlConverterFactory() {}

    private final Map<Type, XmlConverter> converters = new HashMap<>();

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (converters.containsKey(type)) {
            return new BodyConverter(type);
        }
        return null;
    }

    private class BodyConverter implements Converter<ResponseBody, Object> {
        private final Type type;

        BodyConverter(Type type) {
            this.type = type;
        }

        @Override
        public Object convert(@Nonnull ResponseBody body) throws IOException {
            try (InputStream in = body.byteStream()) {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, charset(body));
                parser.nextTag();
                @Nullable XmlRootElement annotation = getRawType(type).getAnnotation(XmlRootElement.class);
                String name = annotation != null ? annotation.name() : getRawType(type).getSimpleName();
                return XmlConverterFactory.this.convert(type, parser, name, XmlPullParser.NO_NAMESPACE);
            } catch (XmlPullParserException e) {
                throw new IOException(e);
            }
        }

        String charset(ResponseBody body) {
            MediaType contentType = body.contentType();
            Charset charset = (contentType != null) ? contentType.charset(Util.UTF_8) : Util.UTF_8;
            return charset.name();
        }
    }

    <T> T convert(Typed<T> type, XmlPullParser parser, @Nonnull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        return convert(type.getType(), parser, name, namespace);
    }

    @SuppressWarnings({"unchecked"})
    <T> T convert(Type type, XmlPullParser parser, @Nonnull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        if (name.contains(XmlProxyConverter.SEPARATOR)) {
            return new XmlProxyConverter<T>(this, type, name)
                    .parseTag(parser, StringUtils.substringBefore(name, XmlProxyConverter.SEPARATOR), namespace);
        }
        XmlConverter converter = getConverter(type);
        if (converter != null) {
            return (T) converter.parseTag(parser, name, namespace);
        } else if (TypeUtils.isAssignable(type, Collection.class)) {
            return new XmlCollectionConverter<T>(this, type).parseTag(parser, name, namespace);
        } else {
            throw new UnsupportedOperationException("No converter found for " + TypeUtils.toString(type));
        }
    }

    /**
     * Fix kvuli nesoumernemu equals pri converters.get
     * @param type
     * @return
     */
    private XmlConverter getConverter(Type type) {
        return converters.entrySet()
                .stream()
                .filter(e -> e.getKey().equals(type))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(() -> converters.get(type));
    }

    @SuppressWarnings("unchecked")
    <T> T convertAttribute(Typed<T> type, String attributeValue) throws IOException {
        XmlConverter converter = getConverter(type.getType());
        if (converter != null) {
            return (T) converter.convertString(attributeValue);
        } else {
            throw new UnsupportedOperationException("No converter found for " + type);
        }
    }

    public static class Builder {
        private Map<Type, XmlConverter> converters = new HashMap<>();

        public Builder() {
            addConverter(new XmlStringConverter());
            addConverter(new XmlDateConverter());
            addConverter(new XmlNumberConverter.ByteConverter(), byte.class, Byte.class);
            addConverter(new XmlNumberConverter.ShortConverter(), short.class, Short.class);
            addConverter(new XmlNumberConverter.IntConverter(), int.class, Integer.class);
            addConverter(new XmlNumberConverter.FloatConverter(), float.class, Float.class);
            addConverter(new XmlNumberConverter.DoubleConverter(), double.class, Double.class);
            addConverter(new XmlNumberConverter.BigIntegerConverter(), BigInteger.class);
            addConverter(new XmlNumberConverter.BigDecimalConverter(), BigDecimal.class);
        }

        public Builder addConverter(XmlConverter converter) {
            return addConverter(converter, converter.getType().getType());
        }

        public Builder addConverterWithWildcard(XmlConverter converter) {
            return addConverterWithWildcard(converter, converter.getType().getType());
        }

        private Builder addConverter(XmlConverter converter, Type type) {
            converters.put(type, converter);
            return this;
        }

        private Builder addConverterWithWildcard(XmlConverter converter, Type type) {
            converters.put(type, converter);
            converters.put(TypeUtils.wildcardType()
                    .withUpperBounds(type)
                    .build(), converter);
            return this;
        }

        private Builder addConverter(XmlConverter converter, Type type1, Type type2) {
            converters.put(type1, converter);
            converters.put(type2, converter);
            return this;
        }

        public XmlConverterFactory build() {
            XmlConverterFactory factory = new XmlConverterFactory();
            for (Map.Entry<Type, XmlConverter> entry : converters.entrySet()) {
                entry.getValue().setXmlConverterFactory(factory);
                factory.converters.put(entry.getKey(), entry.getValue());
            }
            return factory;
        }
    }
}
