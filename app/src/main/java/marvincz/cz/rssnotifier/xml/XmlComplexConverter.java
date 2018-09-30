package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public abstract class XmlComplexConverter<T> extends XmlConverter<T> {

    private final InstanceCreator<T> instanceCreator;

    public XmlComplexConverter() {
        this(new ReflectiveInstanceCreator<>());
    }

    public XmlComplexConverter(InstanceCreator<T> instanceCreator) {
        this.instanceCreator = instanceCreator;
    }

    @Nullable
    protected abstract List<XmlFieldDefinition<T, ?>> getAttributes();

    @Nullable
    protected abstract List<XmlFieldDefinition<T, ?>> getTags();

    public final T convertBody(XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        T result = instanceCreator.createInstance(getType());
        final List<XmlFieldDefinition<T, ?>> attributes = getAttributes();
        final List<XmlFieldDefinition<T, ?>> tags = getTags();

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
            String fieldName = parser.getName();
            String fieldNamespace = parser.getNamespace();

            XmlFieldDefinition tag = matchingField(tags, fieldName, fieldNamespace);
            if (tag != null) {
                convertField(parser, tag, result);
            } else {
                skip(parser);
            }
        } while (parser.getEventType() != XmlPullParser.END_TAG || parser.getDepth() != depth);
        return result;
    }

    private <V> void convertAttribute(String attributeValue, XmlFieldDefinition<T, V> field, T object) throws IOException {
        V converted = xmlConverterFactory.convertAttribute(field.type, attributeValue);
        field.setter.set(object, converted);
    }

    private <V> void convertField(XmlPullParser parser, XmlFieldDefinition<T, V> field, T object) throws IOException, XmlPullParserException {
        V converted = xmlConverterFactory.convert(field.type, parser, field.name, field.namespace);
        field.setter.set(object, converted);
    }

    @Nullable
    private XmlFieldDefinition matchingField(@Nullable List<XmlFieldDefinition<T, ?>> fields, String name, String namespace) {
        if (fields != null) {
            for (XmlFieldDefinition attribute : fields) {
                if (name.equals(StringUtils.substringBefore(attribute.name, XmlProxyConverter.SEPARATOR)) && defaultNamespace(namespace).equals(defaultNamespace(attribute.namespace))) {
                    return attribute;
                }
            }
        }
        return null;
    }
}
