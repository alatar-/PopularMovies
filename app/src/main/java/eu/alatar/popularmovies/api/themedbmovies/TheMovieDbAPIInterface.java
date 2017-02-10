package eu.alatar.popularmovies.api.themedbmovies;

import eu.alatar.popularmovies.api.themedbmovies.models.Movie;
import eu.alatar.popularmovies.api.themedbmovies.models.MovieSetPage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by kam on 10/02/17.
 */

public interface TheMovieDbAPIInterface {

    @GET("movie/{id}")
    Call<Movie> getMovie(@Path("id") int id);

    @GET("movie/popular")
    Call<MovieSetPage> getPopularMovies();

    @GET("movie/top-rated")
    Call<MovieSetPage> getTopRatedMovies();
}
