package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Xml;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

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

import marvincz.cz.rssnotifier.model.Rss;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static okhttp3.internal.Util.UTF_8;

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

    private class BodyConverter implements Converter<ResponseBody, Rss> {
        private final Type type;

        BodyConverter(Type type) {
            this.type = type;
        }

        @Override
        public Rss convert(@NonNull ResponseBody body) throws IOException {
            try (InputStream in = body.byteStream()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, charset(body));
                parser.nextTag();
                return XmlConverterFactory.this.convert(type, parser, parser.getName(), XmlPullParser.NO_NAMESPACE);
            } catch (XmlPullParserException e) {
                throw new IOException(e);
            }
        }

        String charset(ResponseBody body) {
            MediaType contentType = body.contentType();
            Charset charset = (contentType != null) ? contentType.charset(UTF_8) : UTF_8;
            return charset.name();
        }
    }

    <T> T convert(Typed<T> type, XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        return convert(type.getType(), parser, name, namespace);
    }

    @SuppressWarnings({"unchecked"})
    <T> T convert(Type type, XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        if (name.contains(XmlProxyConverter.SEPARATOR)) {
            return new XmlProxyConverter<T>(this, type, name)
                    .parseTag(parser, StringUtils.substringBefore(name, XmlProxyConverter.SEPARATOR), namespace);
        }
        XmlConverter converter = converters.get(type);
        if (converter != null) {
            return (T) converter.parseTag(parser, name, namespace);
        } else if (TypeUtils.isAssignable(type, Collection.class)) {
            return new XmlCollectionConverter<T>(this, type).parseTag(parser, name, namespace);
        } else {
            throw new UnsupportedOperationException("No converter found for " + TypeUtils.toString(type));
        }
    }

    @SuppressWarnings("unchecked")
    <T> T convertAttribute(Typed<T> type, String attributeValue) throws IOException {
        XmlConverter converter = converters.get(type.getType());
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
            addConverter(new XmlUriConverter());
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

        private Builder addConverter(XmlConverter converter, Type type) {
            converters.put(type, converter);
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
