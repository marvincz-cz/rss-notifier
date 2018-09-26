package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;

public class ReflectiveFieldValueSetter<T, V> implements FieldValueSetter<T, V> {
    @NonNull
    private String fieldName;

    public ReflectiveFieldValueSetter(@NonNull String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void set(T object, V value) throws IOException {
        try {
            FieldUtils.writeField(object, fieldName, value);
        } catch (IllegalAccessException e) {
            throw new IOException("Cannot access field " + fieldName, e);
        }
    }
}
