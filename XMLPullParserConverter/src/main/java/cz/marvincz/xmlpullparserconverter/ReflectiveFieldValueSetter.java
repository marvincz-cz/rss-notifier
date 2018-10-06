package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReflectiveFieldValueSetter<T, V> implements FieldValueSetter<T, V> {
    @Nonnull
    private String fieldName;

    public ReflectiveFieldValueSetter(@Nonnull String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void set(@Nonnull T object, @Nullable V value) throws IOException {
        try {
            FieldUtils.writeField(object, fieldName, value);
        } catch (IllegalAccessException e) {
            throw new IOException("Cannot access field " + fieldName, e);
        }
    }
}
