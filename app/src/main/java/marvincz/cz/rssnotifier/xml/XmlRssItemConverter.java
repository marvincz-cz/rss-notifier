package marvincz.cz.rssnotifier.xml;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.util.Arrays;
import java.util.List;

import marvincz.cz.rssnotifier.model.RssItem;

public class XmlRssItemConverter extends XmlComplexConverter<RssItem> {
    public XmlRssItemConverter() {
        super(t -> new RssItem());
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<RssItem, ?>> getAttributes() {
        return null;
    }

    @Override
    protected List<XmlFieldDefinition<RssItem, ?>> getTags() {
        return Arrays.asList(
                new XmlFieldDefinition<>("title", (i, t) -> i.title = t, new TypeLiteral<String>() {}),
                new XmlFieldDefinition<>("description", (i, d) -> i.description = d, new TypeLiteral<String>() {}),
                new XmlFieldDefinition<>("link", (i, l) -> i.link = l, new TypeLiteral<Uri>() {}));
    }

    @Override
    public Typed<RssItem> getType() {
        return new TypeLiteral<RssItem>() {};
    }
}
