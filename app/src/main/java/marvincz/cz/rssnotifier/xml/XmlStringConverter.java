package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

public class XmlStringConverter extends AbstractXmlStringConverter<String> {
    @Override
    public Typed<String> getType() {
        return new TypeLiteral<String>() {};
    }

    @Nullable
    @Override
    protected String convertString(@Nullable String stringValue) {
        return stringValue;
    }
}
