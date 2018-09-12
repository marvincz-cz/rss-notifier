package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.xml.XmlParser;

class RssItem {
    public String title;
    public String description;
    public Uri link;

    public static class Parser extends XmlParser {
        public static RssItem parse(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "item");
            RssItem item = new RssItem();
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                final String name = parser.getName();
                // Starts by looking for the entry tag
                switch (name) {
                    case "title":
                        item.title = readText(parser);
                        break;
                    case "description":
                        item.description = readText(parser);
                        break;
                    case "link":
                        item.link = Uri.parse(readText(parser));
                        break;
                    default:
                        skip(parser);
                }
            }
            return item;
        }
    }
}
