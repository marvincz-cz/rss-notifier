package marvincz.cz.rssnotifier.xml;

import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import marvincz.cz.rssnotifier.model.RssChannel;
import marvincz.cz.rssnotifier.model.RssItem;

public class XmlRssChannelConverter extends XmlConverter<RssChannel> {
    static {
        XmlConverterFactory.register(new XmlRssChannelConverter());
    }

    public RssChannel convertBody(XmlPullParser parser) throws IOException, XmlPullParserException {
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
                    channel.title = XmlConverterFactory.convert(String.class, parser, "title", null);
                    break;
                case "description":
                    channel.description = XmlConverterFactory.convert(String.class, parser, "description", null);
                    break;
                case "link":
                    channel.link = XmlConverterFactory.convert(Uri.class, parser, "link", null);
                    break;
//                    case "lastBuildDate":
//                        channel.lastBuildDate = ZonedDateTime.parse(readText(parser));
//                        break;
                case "item":
                    channel.items.add(XmlConverterFactory.convert(RssItem.class, parser, "item", null));
                    break;
                default:
                    skip(parser);
            }
        }
        return channel;
    }
}
