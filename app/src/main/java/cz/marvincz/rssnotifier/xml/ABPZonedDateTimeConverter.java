package cz.marvincz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import cz.marvincz.xmlpullparserconverter.AbstractXmlStringConverter;

public class ABPZonedDateTimeConverter extends AbstractXmlStringConverter<ZonedDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US);

    @Override
    public Typed<ZonedDateTime> getType() {
        return new TypeLiteral<ZonedDateTime>() {};
    }

    @Nullable
    @Override
    protected ZonedDateTime convertString(@Nullable String stringValue) {
        if (stringValue != null) {
            return ZonedDateTime.from(formatter.parse(stringValue));
        } else {
            return null;
        }
    }
}
