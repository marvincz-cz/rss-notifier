package cz.marvincz.rssnotifier.xml;

import android.net.Uri;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import cz.marvincz.rssnotifier.model.ChannelWithItems;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter;
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition;

public class XmlRssChannelConverter extends XmlComplexConverter<ChannelWithItems> {
    public XmlRssChannelConverter() {
        super(t -> new ChannelWithItems());
    }

    @Override
    public Typed<ChannelWithItems> getType() {
        return new TypeLiteral<ChannelWithItems>() {};
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<ChannelWithItems, ?>> getAttributes() {
        return null;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<ChannelWithItems, ?>> getTags() {
        return Arrays.asList(
                new XmlFieldDefinition<>("title", (ch, t) -> ch.title = t, new TypeLiteral<String>() {}),
                new XmlFieldDefinition<>("description", (ch, d) -> ch.description = d, new TypeLiteral<String>() {}),
                new XmlFieldDefinition<>("link", (ch, l) -> ch.link = l, new TypeLiteral<Uri>() {}),
                new XmlFieldDefinition<>("item", (ch, i) -> ch.items = i, new TypeLiteral<List<RssItem>>() {}));
    }
}
