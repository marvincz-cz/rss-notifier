package cz.marvincz.rssnotifier.xml;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import cz.marvincz.rssnotifier.model.Rss;
import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.xmlpullparserconverter.XmlComplexConverter;
import cz.marvincz.xmlpullparserconverter.XmlFieldDefinition;

public class XmlRssConverter extends XmlComplexConverter<Rss> {


    @Override
    public Typed<Rss> getType() {
        return new TypeLiteral<Rss>() {};
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<Rss, ?>> getAttributes() {
        return null;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<Rss, ?>> getTags() {
        return Collections.singletonList(new XmlFieldDefinition<Rss, RssChannel>("channel", (rss, channel) -> rss.channel = channel, new TypeLiteral<RssChannel>() {}));
    }
}
