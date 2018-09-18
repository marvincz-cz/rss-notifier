package marvincz.cz.rssnotifier.xml;

import android.net.Uri;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import marvincz.cz.rssnotifier.model.RssItem;

public class XmlRssItemConverter extends XmlConverter<RssItem> {
    static {
        XmlConverterFactory.register(new XmlRssItemConverter());
    }

    public RssItem convertBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        RssItem item = new RssItem();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case "title":
                    item.title = XmlConverterFactory.convert(String.class, parser, "title", null);
                    break;
                case "description":
                    item.description = XmlConverterFactory.convert(String.class, parser, "description", null);
                    break;
                case "link":
                    item.link = XmlConverterFactory.convert(Uri.class, parser, "link", null);
                    break;
                default:
                    skip(parser);
            }
        }
        return item;
    }
}
