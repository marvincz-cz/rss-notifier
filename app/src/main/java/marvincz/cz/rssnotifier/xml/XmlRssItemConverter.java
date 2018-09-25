package marvincz.cz.rssnotifier.xml;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.util.ArrayList;
import java.util.List;

import marvincz.cz.rssnotifier.model.RssItem;

public class XmlRssItemConverter extends XmlComplexConverter<RssItem> {
    public XmlRssItemConverter() {
        super(t -> new RssItem());
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition> getAttributes() {
        return null;
    }

    @Override
    protected List<XmlFieldDefinition> getTags() {
        List<XmlFieldDefinition> list = new ArrayList<>();
        list.add(new XmlFieldDefinition("title", new TypeLiteral<String>(){}));
        list.add(new XmlFieldDefinition("description", new TypeLiteral<String>(){}));
        list.add(new XmlFieldDefinition("link", new TypeLiteral<Uri>(){}));
        return list;
    }

    @Override
    public Typed<RssItem> getType() {
        return new TypeLiteral<RssItem>() {};
    }
}
