package marvincz.cz.rssnotifier.xml;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;

public class XmlFieldDefinition<T, V> {
    public final String name;
    public final String namespace;
    public final FieldValueSetter<T, V> setter;
    public final Typed<V> type;

    public XmlFieldDefinition(String name, String namespace, String fieldName, Typed<V> type) {
        this(name, namespace, new ReflectiveFieldValueSetter<>(fieldName), type);
    }

    public XmlFieldDefinition(String fieldName, Typed<V> type) {
        this(fieldName, fieldName, type);
    }

    public XmlFieldDefinition(String name, String fieldName, Typed<V> type) {
        this(name, XmlPullParser.NO_NAMESPACE, fieldName, type);
    }

    public XmlFieldDefinition(String name, FieldValueSetter<T, V> setter, Typed<V> type) {
        this(name, XmlPullParser.NO_NAMESPACE, setter, type);
    }

    public XmlFieldDefinition(String name, String namespace, FieldValueSetter<T, V> setter, Typed<V> type) {
        this.name = name;
        this.namespace = namespace;
        this.setter = setter;
        this.type = type;
    }

    public XmlFieldDefinition(Field field) {
        this(field.getName(), TypeUtils.wrap(field.getGenericType()));
    }
}
