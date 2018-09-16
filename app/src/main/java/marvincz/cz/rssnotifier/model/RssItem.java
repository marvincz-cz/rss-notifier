package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.xml.XmlParser;
import marvincz.cz.rssnotifier.xml.XmlStringParser;

public class RssItem {
    public String title;
    public String description;
    public Uri link;

    public static class Parser extends XmlParser<RssItem> {
        public RssItem parseBody(XmlPullParser parser) throws IOException, XmlPullParserException {
            RssItem item = new RssItem();
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                final String name = parser.getName();
                // Starts by looking for the entry tag
                switch (name) {
                    case "title":
                        item.title = new XmlStringParser().parseTag(parser, "title", null);
                        break;
                    case "description":
                        item.description = new XmlStringParser().parseTag(parser, "description", null);
                        break;
                    case "link":
                        item.link = Uri.parse(new XmlStringParser().parseTag(parser, "link", null));
                        break;
                    default:
                        skip(parser);
                }
            }
            return item;
        }
    }
}
