package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public abstract class XmlComplexConverter<T> extends XmlConverter<T> {

    private final InstanceCreator<T> instanceCreator;
    @Nullable
    private final List<XmlFieldDefinition<T, ?>> attributes;
    @Nullable
    private final List<XmlFieldDefinition<T, ?>> tags;

    public XmlComplexConverter() {
        this(new ReflectiveInstanceCreator<>());
    }

    public XmlComplexConverter(InstanceCreator<T> instanceCreator) {
        this.instanceCreator = instanceCreator;
        attributes = getAttributes();
        tags = getTags();
    }

    @Nullable
    protected abstract List<XmlFieldDefinition<T, ?>> getAttributes();

    @Nullable
    protected abstract List<XmlFieldDefinition<T, ?>> getTags();

    public final T convertBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        T result = instanceCreator.createInstance(getType());

        if ((attributes != null) && !attributes.isEmpty()) {
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                XmlFieldDefinition attribute = matchingField(attributes, parser.getAttributeName(i), parser.getAttributeNamespace(i));
                if (attribute != null) {
                    convertAttribute(parser.getAttributeValue(i), attribute, result);
                }
            }
        }

        parser.next();
        do {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            // Starts by looking for the entry tag
            String name = parser.getName();
            String namespace = parser.getNamespace();

            XmlFieldDefinition tag = matchingField(tags, name, namespace);
            if (tag != null) {
                convertField(parser, name, namespace, tag, result);
            } else {
                skip(parser);
            }
        } while (parser.getEventType() != XmlPullParser.END_TAG || parser.getDepth() != depth);
        return result;
    }

    private <V> void convertAttribute(String attributeValue, XmlFieldDefinition<T, V> field, T object) throws IOException {
        V converted = XmlConverterFactory.convertAttribute(field.type, attributeValue);
        field.setter.set(object, converted);
    }

    private <V> void convertField(XmlPullParser parser, String name, String namespace, XmlFieldDefinition<T, V> field, T object) throws IOException, XmlPullParserException {
        V converted = XmlConverterFactory.convert(field.type, parser, name, namespace);
        field.setter.set(object, converted);
    }

    @Nullable
    private XmlFieldDefinition matchingField(@Nullable List<XmlFieldDefinition<T, ?>> fields, String name, String namespace) {
        if (fields != null) {
            for (XmlFieldDefinition attribute : fields) {
                if (name.equals(attribute.name) && defaultNamespace(namespace).equals(defaultNamespace(attribute.namespace))) {
                    return attribute;
                }
            }
        }
        return null;
    }
}
