package cz.marvincz.rssnotifier.retrofit

import cz.marvincz.rssnotifier.model.xml.XmlRss
import retrofit2.http.GET
import retrofit2.http.Url

interface RestApi {
    @GET
    suspend fun rss(@Url url: String): XmlRss
}
