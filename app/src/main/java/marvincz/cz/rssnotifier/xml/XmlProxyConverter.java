package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;

public class XmlProxyConverter<T> extends XmlConverter<T> {
    public static final String SEPARATOR = "/";
    private final String childPath;
    private final Type type;

    public XmlProxyConverter(XmlConverterFactory xmlConverterFactory, Type type, String path) {
        setXmlConverterFactory(xmlConverterFactory);
        this.childPath = StringUtils.substringAfter(path, SEPARATOR);
        this.type = type;
    }

    @Override
    public Typed<T> getType() {
        return TypeUtils.wrap(type);
    }

    @Nullable
    @Override
    public T convertBody(XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        final String childName = StringUtils.substringBefore(childPath, SEPARATOR);

        parser.next();
        do {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }

            if (parser.getName().equals(childName)) {
                return xmlConverterFactory.convert(type, parser, childPath, namespace);
            } else {
                throw new IOException("Expected tag(s)" + childPath + " Found child tag " + parser.getName());
            }
        } while (parser.getEventType() != XmlPullParser.END_TAG || parser.getDepth() != depth);
        return null;
    }
}
