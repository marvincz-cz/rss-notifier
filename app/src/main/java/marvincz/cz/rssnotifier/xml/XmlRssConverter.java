package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.model.RssChannel;

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
