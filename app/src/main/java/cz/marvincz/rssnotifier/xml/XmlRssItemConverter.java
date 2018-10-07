package cz.marvincz.rssnotifier.xml;

import android.net.Uri;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter;
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition;

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
