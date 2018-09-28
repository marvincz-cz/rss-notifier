package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class XmlConverter<T> {
    public abstract Typed<T> getType();
    protected int depth;
    protected XmlConverterFactory xmlConverterFactory;

    void setXmlConverterFactory(XmlConverterFactory xmlConverterFactory) {
        this.xmlConverterFactory = xmlConverterFactory;
    }

    @Nullable
    public final T parseTag(XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, name);
        depth = parser.getDepth();
        T result = convertBody(parser);
        if ((depth > 1) && (parser.getEventType() == XmlPullParser.END_TAG) && (parser.getDepth() == depth)) {
            parser.next();
        }
        return result;
    }

    @Nullable
    protected T convertString(@Nullable String stringValue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public abstract T convertBody(XmlPullParser parser) throws IOException, XmlPullParserException;

    protected final void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        final int skipDepth = parser.getDepth();
        while (parser.next() != XmlPullParser.END_TAG || skipDepth != parser.getDepth());
        parser.next(); // move past
    }

    protected String defaultNamespace(@Nullable String string) {
        return (string == null) ? XmlPullParser.NO_NAMESPACE : string;
    }
}
