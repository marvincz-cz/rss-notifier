package marvincz.cz.rssnotifier.xml;

import android.net.Uri;
import android.support.annotation.Nullable;

public class XmlUriConverter extends AbstractXmlStringConverter<Uri> {
    static {
        XmlConverterFactory.register(new XmlUriConverter());
    }

    @Nullable
    @Override
    protected Uri convertString(@Nullable String stringValue) {
        if (stringValue != null) {
            return Uri.parse(stringValue);
        }
        return null;
    }
}
