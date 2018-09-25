package marvincz.cz.rssnotifier.xml;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import org.threeten.bp.ZonedDateTime;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import marvincz.cz.rssnotifier.model.RssChannel;
import marvincz.cz.rssnotifier.model.RssItem;

public class XmlRssChannelConverter extends XmlComplexConverter<RssChannel> {
    public XmlRssChannelConverter() {
        super(t -> new RssChannel());
    }

    @Override
    public Typed<RssChannel> getType() {
        return new TypeLiteral<RssChannel>() {};
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition> getAttributes() {
        return null;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition> getTags() {
        return Arrays.asList(
                new XmlFieldDefinition("title", new TypeLiteral<String>(){}),
                new XmlFieldDefinition("description", new TypeLiteral<String>(){}),
                new XmlFieldDefinition("link", new TypeLiteral<Uri>(){}),
//                new XmlFieldDefinition("lastBuildDate", new TypeLiteral<ZonedDateTime>(){}),
                new XmlFieldDefinition("item", "items", new TypeLiteral<List<RssItem>>(){}));
    }
}
