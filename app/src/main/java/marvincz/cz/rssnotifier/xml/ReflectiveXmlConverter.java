package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveXmlConverter<T> extends XmlComplexConverter<T> {
    @NonNull
    private final Class<T> clazz;

    public ReflectiveXmlConverter(@NonNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<T, ?>> getAttributes() {
        return null;
    }

    @Nullable
    @Override
    protected List<XmlFieldDefinition<T, ?>> getTags() {
        List<XmlFieldDefinition<T, ?>> list = new ArrayList<>();
        for (Field field : FieldUtils.getAllFieldsList(clazz)) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isTransient(modifiers)
                    && !Modifier.isStatic(modifiers)
                    && !Modifier.isFinal(modifiers)) {
                list.add(new XmlFieldDefinition<>(field));
            }
        }
        return list;
    }

    @Override
    public Typed<T> getType() {
        return TypeUtils.wrap(clazz);
    }
}
