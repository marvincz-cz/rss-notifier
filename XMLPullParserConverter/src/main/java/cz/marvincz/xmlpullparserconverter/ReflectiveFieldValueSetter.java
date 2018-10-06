package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReflectiveFieldValueSetter<T, V> implements FieldValueSetter<T, V> {
    private final String fieldName;
    private final Field field;

    public ReflectiveFieldValueSetter(@Nonnull String fieldName) {
        this.fieldName = fieldName;
        field = null;
    }

    public ReflectiveFieldValueSetter(@Nonnull Field field) {
        this.fieldName = null;
        this.field = field;
    }

    @Override
    public void set(@Nonnull T object, @Nullable V value) throws IOException {
        if (field == null) {
            set(object, value, fieldName);
        } else {
            set(object, value, field);
        }
    }

    private static <T, V> void set(@Nonnull T object, @Nullable V value, String fieldName) throws IOException {
        try {
            FieldUtils.writeField(object, fieldName, value);
        } catch (IllegalAccessException e) {
            throw new IOException("Cannot access field " + fieldName, e);
        }
    }

    private static <T, V> void set(@Nonnull T object, @Nullable V value, Field field) throws IOException {
        try {
            FieldUtils.writeField(field, object, value);
        } catch (IllegalAccessException e) {
            throw new IOException("Cannot access field " + field, e);
        }
    }
}
