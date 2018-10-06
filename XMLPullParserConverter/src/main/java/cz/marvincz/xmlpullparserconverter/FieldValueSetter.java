package cz.marvincz.xmlpullparserconverter;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FieldValueSetter<T, V> {
    void set(@Nonnull T object, @Nullable V value) throws IOException;
}
