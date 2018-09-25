package marvincz.cz.rssnotifier.xml;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ReflectiveInstanceCreator<T> implements InstanceCreator<T> {
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
