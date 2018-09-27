package marvincz.cz.rssnotifier.retrofit;

import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.model.RssChannel;
import marvincz.cz.rssnotifier.model.RssItem;
import marvincz.cz.rssnotifier.xml.ReflectiveXmlConverter;
import marvincz.cz.rssnotifier.xml.XmlConverterFactory;
import marvincz.cz.rssnotifier.xml.XmlRssChannelConverter;
import marvincz.cz.rssnotifier.xml.XmlRssConverter;
import marvincz.cz.rssnotifier.xml.XmlRssItemConverter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Client {
    public static RestApi call(String baseUrl) {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(new XmlConverterFactory.Builder()
                        .addConverter(new XmlRssConverter())
                        .addConverter(new XmlRssItemConverter())
                        .addConverter(new XmlRssChannelConverter())
                        .build())
                .build().create(RestApi.class);
    }

    public static RestApi callReflect(String baseUrl) {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(new XmlConverterFactory.Builder()
                        .addConverter(new ReflectiveXmlConverter<>(Rss.class))
                        .addConverter(new ReflectiveXmlConverter<>(RssItem.class))
                        .addConverter(new ReflectiveXmlConverter<>(RssChannel.class))
                        .build())
                .build().create(RestApi.class);
    }
}
