package marvincz.cz.rssnotifier.xml;

import org.apache.commons.lang3.reflect.Typed;

public class XmlFieldDefinition {
    public String name;
    public String namespace;
    public String field;
    public Typed type;

    public XmlFieldDefinition(String name, String namespace, String field, Typed type) {
        this.name = name;
        this.namespace = namespace;
        this.field = field;
        this.type = type;
    }

    public XmlFieldDefinition(String field, Typed type) {
        this.field = field;
        this.name = field;
        this.type = type;
    }

    public XmlFieldDefinition(String name, String field, Typed type) {
        this.name = name;
        this.field = field;
        this.type = type;
    }
}
