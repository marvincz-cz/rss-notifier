package marvincz.cz.rssnotifier.xml;

import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public abstract class XmlComplexConverter<T> extends XmlConverter<T> {

    private final InstanceCreator<T> instanceCreator;
    @Nullable
    private final List<XmlFieldDefinition> attributes;
    @Nullable
    private final List<XmlFieldDefinition> tags;

    public XmlComplexConverter() {
        this(new ReflectiveInstanceCreator<>());
    }

    public XmlComplexConverter(InstanceCreator<T> instanceCreator) {
        this.instanceCreator = instanceCreator;
        attributes = getAttributes();
        tags = getTags();
    }

    @Nullable
    protected abstract List<XmlFieldDefinition> getAttributes();

    @Nullable
    protected abstract List<XmlFieldDefinition> getTags();

    public final T convertBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        T result = instanceCreator.createInstance(getType());

        if ((attributes != null) && !attributes.isEmpty()) {
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                XmlFieldDefinition attribute = matchingField(attributes, parser.getAttributeName(i), parser.getAttributeNamespace(i));
                if (attribute != null) {
                    @SuppressWarnings("unchecked")
                    Object converted = XmlConverterFactory.convertAttribute(attribute.type, parser.getAttributeValue(i));
                    try {
                        FieldUtils.writeField(result, attribute.field, converted);
                    } catch (IllegalAccessException e) {
                        throw new IOException("Cannot access field " + TypeUtils.toString(getType().getType()) + '.' + attribute.field, e);
                    }
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
                @SuppressWarnings("unchecked")
                Object converted = XmlConverterFactory.convert(tag.type, parser, name, namespace);
                try {
                    FieldUtils.writeField(result, tag.field, converted);
                } catch (IllegalAccessException e) {
                    throw new IOException("Cannot access field " + TypeUtils.toString(getType().getType()) + '.' + tag.field, e);
                }
            } else {
                skip(parser);
            }
        } while (parser.getEventType() != XmlPullParser.END_TAG || parser.getDepth() != depth);
        return result;
    }

    @Nullable
    private XmlFieldDefinition matchingField(@Nullable List<XmlFieldDefinition> fields, String name, String namespace) {
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
