package marvincz.cz.rssnotifier.xml;

import java.io.IOException;

public interface FieldValueSetter<T, V> {
    void set(T object, V value) throws IOException;
}
