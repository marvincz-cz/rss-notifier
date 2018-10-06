package marvincz.cz.rssnotifier.retrofit;

import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.model.RssChannel;
import marvincz.cz.rssnotifier.model.RssItem;
import marvincz.cz.rssnotifier.xml.ABPZonedDateTimeConverter;
import marvincz.cz.rssnotifier.xml.ReflectiveXmlConverter;
import marvincz.cz.rssnotifier.xml.XmlConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Client {
    public static RestApi call() {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://127.0.0.1")
                .addConverterFactory(new XmlConverterFactory.Builder()
                        .addConverter(new ReflectiveXmlConverter<>(Rss.class))
                        .addConverter(new ReflectiveXmlConverter<>(RssItem.class))
                        .addConverter(new ReflectiveXmlConverter<>(RssChannel.class))
                        .addConverter(new ABPZonedDateTimeConverter())
                        .build())
                .build().create(RestApi.class);
    }
}
