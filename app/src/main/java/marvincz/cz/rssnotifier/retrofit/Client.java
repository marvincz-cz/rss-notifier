package marvincz.cz.rssnotifier.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class Client {
    public static RestApi call(String baseUrl) {
        return new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(ConverterFactory.INSTANCE)
                .build().create(RestApi.class);
    }
}
