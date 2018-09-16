package marvincz.cz.rssnotifier.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.xml.XmlParser;

public class Rss {
    public RssChannel channel;

    public static class Parser extends XmlParser<Rss> {
        public Rss parseBody(XmlPullParser parser) throws IOException, XmlPullParserException {
            Rss item = new Rss();
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                final String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("channel")) {
                    item.channel = new RssChannel.Parser().parseTag(parser, "channel", null);
                } else {
                    skip(parser);
                }
            }
            return item;
        }
    }
}
