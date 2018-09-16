package marvincz.cz.rssnotifier.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.xml.XmlParser;

public class Rss {
    public RssChannel channel;

    public static class Parser extends XmlParser {
        public static Rss parse(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "rss");
            Rss item = new Rss();
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                final String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("channel")) {
                    item.channel = RssChannel.Parser.parse(parser);
                } else {
                    skip(parser);
                }
            }
            return item;
        }
    }
}
