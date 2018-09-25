package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class XmlConverterFactory {
    private static final Map<Type, XmlConverter> converters = new HashMap<>();
    static {
        register(new XmlRssConverter());
        register(new XmlRssChannelConverter());
        register(new XmlRssItemConverter());
        register(new XmlStringConverter());
        register(new XmlUriConverter());
    }

    public static <T> void register(XmlConverter<T> converter) {
        converters.put(converter.getType().getType(), converter);
    }

    public static <T> T convert(Typed<T> type, XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        return convert(type.getType(), parser, name, namespace);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public static <T> T convert(Type type, XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        XmlConverter converter = converters.get(type);
        if (converter != null) {
            return (T) converter.parseTag(parser, name, namespace);
        } else if (TypeUtils.isAssignable(type, Collection.class)) {
            return new XmlCollectionConverter<T>(type).parseTag(parser, name, namespace);
        } else {
            throw new UnsupportedOperationException("No converter found for " + TypeUtils.toString(type));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertAttribute(Typed<T> type, String attributeValue) {
        XmlConverter converter = converters.get(type.getType());
        if (converter != null) {
            return (T) converter.convertString(attributeValue);
        } else {
            throw new UnsupportedOperationException("No converter found for " + type);
        }
    }
}
