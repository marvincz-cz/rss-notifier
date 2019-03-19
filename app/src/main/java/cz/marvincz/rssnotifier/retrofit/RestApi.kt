package cz.marvincz.rssnotifier.retrofit

import cz.marvincz.rssnotifier.model.ChannelWithItems
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RestApi {
    @GET
    fun rss(@Url url: String): Call<ChannelWithItems>
}
