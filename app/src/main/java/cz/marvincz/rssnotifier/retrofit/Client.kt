package cz.marvincz.rssnotifier.retrofit

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Client {
    fun call(): RestApi {
        return Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl("http://127.0.0.1")
                .addConverterFactory(TikXmlConverterFactory.create(
                        TikXml.Builder()
                                .exceptionOnUnreadXml(false)
                                .build())
                )
                .build()
                .create(RestApi::class.java)
    }
}
