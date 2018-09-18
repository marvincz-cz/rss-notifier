package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class XmlConverter<T> {
    public final Typed<T> getType() {
        return new TypeLiteral<T>(){};
    }

    @Nullable
    public final T parseTag(XmlPullParser parser, @Nullable String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, name);
        T result = convertBody(parser);
        parser.require(XmlPullParser.END_TAG, namespace, name);
        return result;
    }

    @Nullable
    protected T convertString(@Nullable String stringValue) {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public abstract T convertBody(XmlPullParser parser) throws IOException, XmlPullParserException;

    protected final void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
