package cz.marvincz.rssnotifier.retrofit;

import cz.marvincz.rssnotifier.model.RssChannel;
import cz.marvincz.rssnotifier.model.RssItem;
import cz.marvincz.rssnotifier.xml.ABPZonedDateTimeConverter;
import cz.marvincz.rssnotifier.xml.XmlUriConverter;
import cz.marvincz.xmlpullparserconverter.ReflectiveXmlConverter;
import cz.marvincz.xmlpullparserconverter.XmlConverterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Client {
    public static RestApi call() {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl("http://127.0.0.1")
                .addConverterFactory(new XmlConverterFactory.Builder()
                        .addConverter(new ReflectiveXmlConverter<>(RssItem.class))
                        .addConverter(new ReflectiveXmlConverter<>(RssChannel.class))
                        .addConverter(new ABPZonedDateTimeConverter())
                        .addConverter(new XmlUriConverter())
                        .build())
                .build().create(RestApi.class);
    }
}
