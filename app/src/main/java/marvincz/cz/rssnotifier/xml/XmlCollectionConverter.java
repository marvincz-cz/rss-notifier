package marvincz.cz.rssnotifier.xml;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

class XmlCollectionConverter<T> extends XmlConverter<T> {
    private final InstanceCreator<T> instanceCreator;
    private final Type type;

    XmlCollectionConverter(XmlConverterFactory xmlConverterFactory, Type type) {
        this(xmlConverterFactory, new CollectionInstanceCreator<>(), type);
    }

    private XmlCollectionConverter(XmlConverterFactory xmlConverterFactory, InstanceCreator<T> instanceCreator, Type type) {
        this.xmlConverterFactory = xmlConverterFactory;
        this.instanceCreator = instanceCreator;
        this.type = type;
    }

    @Override
    public Typed<T> getType() {
        return TypeUtils.wrap(type);
    }

    @Nullable
    @Override
    public T convertBody(XmlPullParser parser, @NonNull String name, @Nullable String namespace) throws IOException, XmlPullParserException {
        T result = instanceCreator.createInstance(getType());
        Type itemType = getCollectionElementType(type);

        int eventType;
        boolean exit = false;
        do {
            eventType = parser.getEventType();

            if ((eventType != XmlPullParser.START_TAG) && (eventType != XmlPullParser.END_TAG)) {
                parser.next();
                continue;
            }

            if ((eventType == XmlPullParser.START_TAG)
                    && name.equals(parser.getName())
                    && defaultNamespace(namespace).equals(defaultNamespace(parser.getNamespace()))) {
                ((Collection) result).add(xmlConverterFactory.convert(itemType, parser, name, namespace));
            } else {
                exit = true;
            }
        } while (!exit);
        return result;
    }

    private Type getCollectionElementType(Type type) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(type, Collection.class);
        for (Map.Entry<TypeVariable<?>, Type> entry : typeArguments.entrySet()) {
            GenericDeclaration genericDeclaration = entry.getKey().getGenericDeclaration();
            if (genericDeclaration instanceof Type && TypeUtils.equals((Type) genericDeclaration, Collection.class)) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("Could not determine element type of Collection " + TypeUtils.toString(type));
    }

    private static class CollectionInstanceCreator<T> extends ReflectiveInstanceCreator<T> {
        private static final Map<Class, InstanceCreator> known = new HashMap<>();
        static {
            known.put(List.class, t -> new ArrayList<>());
            known.put(ArrayList.class, t -> new ArrayList<>());
            known.put(Vector.class, t -> new Vector<>());
            known.put(Stack.class, t -> new Stack<>());
            known.put(LinkedList.class, t -> new LinkedList<>());

            known.put(Set.class, t -> new HashSet<>());
            known.put(HashSet.class, t -> new HashSet<>());
            known.put(LinkedHashSet.class, t -> new LinkedHashSet<>());
            known.put(SortedSet.class, t -> new TreeSet<>());
            known.put(NavigableSet.class, t -> new TreeSet<>());
            known.put(TreeSet.class, t -> new TreeSet<>());

            known.put(Queue.class, t -> new ArrayDeque<>());
            known.put(Deque.class, t -> new ArrayDeque<>());
            known.put(ArrayDeque.class, t -> new ArrayDeque<>());
        }

        @Override
        public T createInstance(Typed<T> type) throws IOException {
            Class<?> rawType = TypeUtils.getRawType(type.getType(), null);
            InstanceCreator knownCreator = known.get(rawType);
            if (knownCreator != null) {
                return (T) knownCreator.createInstance(type);
            } else {
                return super.createInstance(type);
            }
        }
    }
}
