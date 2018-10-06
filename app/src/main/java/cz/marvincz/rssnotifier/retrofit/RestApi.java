package cz.marvincz.rssnotifier.retrofit;

import cz.marvincz.rssnotifier.model.RssChannel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RestApi {
    @GET()
    Call<RssChannel> rss(@Url String url);
}
