package cz.marvincz.rssnotifier.retrofit

import cz.marvincz.rssnotifier.xml.ABPZonedDateTimeConverter
import cz.marvincz.rssnotifier.xml.XmlChannelWithItemsConverter
import cz.marvincz.rssnotifier.xml.XmlRssItemConverter
import cz.marvincz.rssnotifier.xml.XmlUriConverter
import cz.marvincz.xmlpullparserconverter.XmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Client {
    fun call(): RestApi {
        return Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("http://127.0.0.1")
                .addConverterFactory(XmlConverterFactory.Builder()
                        .addConverterWithWildcard(XmlRssItemConverter())
                        .addConverter(XmlChannelWithItemsConverter())
                        .addConverter(ABPZonedDateTimeConverter())
                        .addConverter(XmlUriConverter())
                        .build())

                .build().create(RestApi::class.java)
    }
}
