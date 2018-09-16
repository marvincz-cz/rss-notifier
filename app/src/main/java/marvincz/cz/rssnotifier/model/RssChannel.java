package marvincz.cz.rssnotifier.model;

import android.net.Uri;

import org.threeten.bp.ZonedDateTime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import marvincz.cz.rssnotifier.xml.XmlParser;
import marvincz.cz.rssnotifier.xml.XmlStringParser;

public class RssChannel {
    public String title;
    public String description;
    public Uri link;
    public ZonedDateTime lastBuildDate;
    public List<RssItem> items;

    public static class Parser extends XmlParser<RssChannel> {
        public RssChannel parseBody(XmlPullParser parser) throws IOException, XmlPullParserException {
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
                        channel.title = new XmlStringParser().parseTag(parser, "title", null);
                        break;
                    case "description":
                        channel.description = new XmlStringParser().parseTag(parser, "description", null);
                        break;
                    case "link":
                        channel.link = Uri.parse(new XmlStringParser().parseTag(parser, "link", null));
                        break;
//                    case "lastBuildDate":
//                        channel.lastBuildDate = ZonedDateTime.parse(readText(parser));
//                        break;
                    case "item":
                        channel.items.add(new RssItem.Parser().parseTag(parser, "item", null));
                        break;
                    default:
                        skip(parser);
                }
            }
            return channel;
        }
    }
}
