package eu.alatar.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import eu.alatar.popularmovies.api.themedbmovies.TheMovieDbAPIInterface;
import eu.alatar.popularmovies.api.themedbmovies.TheMovieDbAPIService;
import eu.alatar.popularmovies.api.themedbmovies.models.Movie;
import eu.alatar.popularmovies.api.themedbmovies.models.MovieSetPage;
import eu.alatar.popularmovies.preferences.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        // Initialize Movie List RecyclerView
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter();
        mMoviesRecyclerView.setAdapter(mMovieListAdapter);

        // Initialize API
        TheMovieDbAPIInterface apiService = TheMovieDbAPIService.getAPIInterface();

        // Test the API
        Call<MovieSetPage> call = apiService.getPopularMovies();
        call.enqueue(new Callback<MovieSetPage>() {
            @Override
            public void onResponse(Call<MovieSetPage> call, Response<MovieSetPage> response) {
                int statusCode = response.code();
                MovieSetPage page = response.body();
                Log.v(Preferences.TAG, "API request successful");
                if (page != null) {
                    for (Movie movie: page.getResults()) {
                        Log.d(Preferences.TAG, movie.getTitle());
                    }
                } else {
                    Log.e(Preferences.TAG, "Body is empty!");
                }
            }

            @Override
            public void onFailure(Call<MovieSetPage> call, Throwable t) {
                Log.e(Preferences.TAG, "API request failed");
                Log.e(Preferences.TAG, t.getMessage());
            }
        });
    }

}
