package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class AbstractXmlStringConverter<T> extends XmlConverter<T> {
    @Override
    @Nullable
    public final T convertBody(XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        T result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = convertString(parser.getText());
            parser.nextTag();
        }
        return result;
    }
}
