package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cz.marvincz.xmlpullparserconverter.annotation.XmlAttribute;
import cz.marvincz.xmlpullparserconverter.annotation.XmlElement;

public class ReflectiveXmlConverter<T> extends XmlComplexConverter<T> {
    @Nonnull
    private final Class<T> clazz;

    public ReflectiveXmlConverter(@Nonnull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<T, ?>> getAttributes() {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, XmlAttribute.class);

        if (fields.isEmpty()) {
            return null;
        }

        List<XmlFieldDefinition<T, ?>> list = new ArrayList<>();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isTransient(modifiers)
                    && !Modifier.isStatic(modifiers)
                    && !Modifier.isFinal(modifiers)) {
                String name = field.getAnnotation(XmlAttribute.class).name();
                if (name.equals("")) {
                    list.add(new XmlFieldDefinition<>(field));
                } else {
                    list.add(new XmlFieldDefinition<>(field, name));
                }
            }
        }
        return list;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<T, ?>> getTags() {
        List<XmlFieldDefinition<T, ?>> list = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(clazz)) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isTransient(modifiers)
                    && !Modifier.isStatic(modifiers)
                    && !Modifier.isFinal(modifiers)
                    && !field.isAnnotationPresent(XmlAttribute.class)) {
                XmlElement annotation = field.getAnnotation(XmlElement.class);
                if (annotation == null) {
                    list.add(new XmlFieldDefinition<>(field));
                } else {
                    list.add(new XmlFieldDefinition<>(field, annotation.name()));
                }
            }
        }
        return list;
    }

    @Override
    public Typed<T> getType() {
        return TypeUtils.wrap(clazz);
    }
}
