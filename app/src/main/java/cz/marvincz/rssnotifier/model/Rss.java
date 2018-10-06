package cz.marvincz.rssnotifier.model;

import cz.marvincz.xmlpullparserconverter.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {
    public RssChannel channel;
}
