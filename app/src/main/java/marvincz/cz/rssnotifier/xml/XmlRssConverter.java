package marvincz.cz.rssnotifier.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.model.RssChannel;

public class XmlRssConverter extends XmlConverter<Rss> {
    static {
        XmlConverterFactory.register(new XmlRssConverter());
    }

    public Rss convertBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        Rss item = new Rss();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("channel")) {
                item.channel = XmlConverterFactory.convert(RssChannel.class, parser, "channel", null);
            } else {
                skip(parser);
            }
        }
        return item;
    }
}
