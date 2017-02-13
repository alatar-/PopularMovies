package eu.alatar.popularmovies.rest;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import eu.alatar.popularmovies.preferences.Credentials;
import eu.alatar.popularmovies.preferences.Preferences;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kam on 13/02/17.
 */

public class RestBuilder {

    public static APIInterface createAPInterfaceInstance() {
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
                .baseUrl(Preferences.THE_MOVIE_DB_API_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        // Initilize the interface
        return retrofit.create(APIInterface.class);
    }
}
