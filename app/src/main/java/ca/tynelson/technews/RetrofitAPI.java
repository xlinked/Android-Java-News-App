package ca.tynelson.technews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    Call<NewsModel> getALLNews(@Url String url);

    @GET
    Call<NewsModel>getNewsByCategory(@Url String url);
}
