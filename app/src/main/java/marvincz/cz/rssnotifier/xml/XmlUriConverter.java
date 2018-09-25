package marvincz.cz.rssnotifier.xml;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

public class XmlUriConverter extends AbstractXmlStringConverter<Uri> {
    @Override
    public Typed<Uri> getType() {
        return new TypeLiteral<Uri>() {};
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
