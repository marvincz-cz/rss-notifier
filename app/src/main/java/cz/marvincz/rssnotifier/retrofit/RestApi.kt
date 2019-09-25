package cz.marvincz.rssnotifier.retrofit

import cz.marvincz.rssnotifier.model.Rss
import retrofit2.http.GET
import retrofit2.http.Url

interface RestApi {
    @GET
    suspend fun rss(@Url url: String): Rss
}
