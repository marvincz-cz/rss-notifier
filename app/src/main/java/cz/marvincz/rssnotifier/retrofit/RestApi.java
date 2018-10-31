package cz.marvincz.rssnotifier.retrofit;

import android.net.Uri;

import cz.marvincz.rssnotifier.model.ChannelWithItems;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RestApi {
    @GET()
    Call<ChannelWithItems> rss(@Url Uri url);
}
