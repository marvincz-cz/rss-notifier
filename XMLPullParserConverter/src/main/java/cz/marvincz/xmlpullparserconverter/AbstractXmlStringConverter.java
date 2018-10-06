package cz.marvincz.xmlpullparserconverter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractXmlStringConverter<T> extends XmlConverter<T> {
    @Override
    @Nullable
    public final T convertBody(XmlPullParser parser, @Nonnull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        T result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = convertString(parser.getText());
            parser.nextTag();
        }
        return result;
    }
}
