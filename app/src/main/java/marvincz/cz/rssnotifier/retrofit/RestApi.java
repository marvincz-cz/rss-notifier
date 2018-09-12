package marvincz.cz.rssnotifier.retrofit;

import marvincz.cz.rssnotifier.model.Rss;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {
    @GET("oots.rss")
    Call<Rss> rss();
}
