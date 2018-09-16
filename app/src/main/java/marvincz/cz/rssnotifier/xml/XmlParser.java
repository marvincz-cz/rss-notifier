package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class XmlParser<T> {

    @Nullable
    public final T parseTag(XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, name);
        T result = parseBody(parser);
        parser.require(XmlPullParser.END_TAG, namespace, name);
        return result;
    }

    @Nullable
    public final T parseAttribute(XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, null);
        T result = parseString(parser.getAttributeValue(namespace, name));
        parser.require(XmlPullParser.END_TAG, namespace, name);
        return result;
    }

    protected T parseString(@Nullable String stringValue) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public abstract T parseBody(XmlPullParser parser) throws IOException, XmlPullParserException;

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
