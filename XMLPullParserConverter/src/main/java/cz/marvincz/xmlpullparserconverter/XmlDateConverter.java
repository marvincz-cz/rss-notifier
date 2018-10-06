package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public class XmlDateConverter extends AbstractXmlStringConverter<Date> {
    private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat get() {
            return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        }
    };

    @Override
    public Typed<Date> getType() {
        return new TypeLiteral<Date>() {};
    }

    @Nullable
    @Override
    protected Date convertString(@Nullable String stringValue) throws IOException {
        if (stringValue != null) {
            try {
                return formatter.get().parse(stringValue);
            } catch (ParseException e) {
                throw new IOException(e);
            }
        } else {
            return null;
        }
    }
}
