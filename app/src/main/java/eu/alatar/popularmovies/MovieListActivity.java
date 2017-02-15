package eu.alatar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import eu.alatar.popularmovies.preferences.Preferences;
import eu.alatar.popularmovies.rest.APIInterface;
import eu.alatar.popularmovies.rest.RestService;
import eu.alatar.popularmovies.rest.models.Movie;
import eu.alatar.popularmovies.rest.models.MovieSet;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends AppCompatActivity implements MovieListAdapter.MovieListClickHandler {

    private APIInterface mAPIInterface;

    private RecyclerView mMoviesRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        // Initialize Movie List RecyclerView
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieListAdapter);

        // Initilize API interface
        mAPIInterface = RestService.getAPIService().mAPIInterface;

        // Populate data
        populateData();
    }

    private void populateData() {
        Log.d(Preferences.TAG, "Requesting movie data from API...");
        Observable<MovieSet> request = mAPIInterface.getPopularMovies();
        request.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviesSet -> {
                    if (moviesSet != null) {
                        Log.d(Preferences.TAG, "Request successful. Displaying obtained movie posters...");
                        mMovieListAdapter.addMovies(moviesSet.getResults());
                    } else {
                        Log.e(Preferences.TAG, "Body is empty!");
                    }
                });
    }

    @Override
    public void onClick(Movie movie) {
        Log.d(Preferences.TAG, "\"" + movie.getTitle() + "\" clicked, starting DetailActivity...");

        Context context = getApplicationContext();
        Class destinationActivity = MovieDetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}
