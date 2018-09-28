package marvincz.cz.rssnotifier.retrofit;

import marvincz.cz.rssnotifier.model.Rss;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RestApi {
    @GET()
    Call<Rss> rss(@Url String url);
}
