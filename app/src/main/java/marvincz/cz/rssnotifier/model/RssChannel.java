package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import org.threeten.bp.ZonedDateTime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import marvincz.cz.rssnotifier.xml.XmlParser;

public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    public ZonedDateTime lastBuildDate;
    public List<RssItem> items;

    public static class Parser extends XmlParser {
        public static RssChannel parse(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "channel");
            RssChannel channel = new RssChannel();
            channel.items = new ArrayList<>();
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                final String name = parser.getName();
                // Starts by looking for the entry tag
                switch (name) {
                    case "title":
                        channel.title = readText(parser);
                        break;
                    case "description":
                        channel.description = readText(parser);
                        break;
                    case "link":
                        channel.link = Uri.parse(readText(parser));
                        break;
//                    case "lastBuildDate":
//                        channel.lastBuildDate = ZonedDateTime.parse(readText(parser));
//                        break;
                    case "item":
                        channel.items.add(RssItem.Parser.parse(parser));
                        break;
                    default:
                        skip(parser);
                }
            }
            return channel;
        }
    }
}
