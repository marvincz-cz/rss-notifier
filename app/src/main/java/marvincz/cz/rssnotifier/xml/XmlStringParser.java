package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class XmlStringParser extends XmlParser<String> {
    @Override
    @Nullable
    public final String parseBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parseString(parser.getText());
            parser.nextTag();
        }
        return result;
    }

    @Override
    protected String parseString(@Nullable String stringValue) {
        return stringValue;
    }
}
