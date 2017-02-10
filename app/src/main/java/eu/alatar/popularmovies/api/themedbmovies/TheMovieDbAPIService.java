package eu.alatar.popularmovies.api.themedbmovies;

import java.io.IOException;

import eu.alatar.popularmovies.preferences.Credentials;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kam on 10/02/17.
 */

public class TheMovieDbAPIService {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";

    private static TheMovieDbAPIInterface sAPIServiceInstance = null;

    public static TheMovieDbAPIInterface getAPIInterface() {
        if (sAPIServiceInstance == null) {
            createAPIServiceInstance();
        }
        return sAPIServiceInstance;
    }


    private static void createAPIServiceInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // Add logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        // Append API key to each request
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder()
                        .addQueryParameter("api_key", Credentials.THE_MOVIE_DB_API_KEY)
                        .build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        // Build retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        // Initilize the interface
        sAPIServiceInstance =
                retrofit.create(TheMovieDbAPIInterface.class);
    }

}
