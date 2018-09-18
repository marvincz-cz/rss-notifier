package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

public class XmlStringConverter extends AbstractXmlStringConverter<String> {
    static {
        XmlConverterFactory.register(new XmlStringConverter());
    }

    @Nullable
    @Override
    protected String convertString(@Nullable String stringValue) {
        return stringValue;
    }
}
