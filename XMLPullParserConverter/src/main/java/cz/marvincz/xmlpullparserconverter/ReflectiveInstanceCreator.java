package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nonnull;

public class ReflectiveInstanceCreator<T> implements InstanceCreator<T> {
    @Override
    @Nonnull
    @SuppressWarnings("unchecked")
    public T createInstance(Typed<T> type) throws IOException {
        Class<?> rawType = TypeUtils.getRawType(type.getType(), null);
        try {
            return (T) ConstructorUtils.invokeExactConstructor(rawType);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IOException("Coould not instantiate type " + TypeUtils.toString(type.getType()), e);
        }
    }
}
