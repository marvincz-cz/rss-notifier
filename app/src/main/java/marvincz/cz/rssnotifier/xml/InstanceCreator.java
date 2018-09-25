package marvincz.cz.rssnotifier.xml;

import org.apache.commons.lang3.reflect.Typed;

import java.io.IOException;

public interface InstanceCreator<T> {
    T createInstance(Typed<T> type) throws IOException;
}
