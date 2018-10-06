package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.Typed;

import java.io.IOException;

import javax.annotation.Nonnull;

public interface InstanceCreator<T> {
    @Nonnull
    T createInstance(Typed<T> type) throws IOException;
}
