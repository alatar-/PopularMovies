package eu.alatar.popularmovies.rest;

import eu.alatar.popularmovies.rest.models.Movie;
import eu.alatar.popularmovies.rest.models.MovieSet;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kam on 10/02/17.
 */

public interface APIInterface {

    @GET("movie/{id}")
    Observable<Movie> getMovie(@Path("id") int id);

    @GET("movie/popular")
    Observable<MovieSet> getPopularMovies();

    @GET("movie/top-rated")
    Call<MovieSet> getTopRatedMovies();
}
