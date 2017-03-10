package eu.alatar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


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

    private MenuItem sortOrder;

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
        MenuItem actionDefaultSortOrder = (MenuItem) findViewById(R.id.menu_sort_most_popular);
        populateData(actionDefaultSortOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    private void populateData(boolean type) {
        Log.d(Preferences.TAG, "Requesting movie data from API...");
        Observable<MovieSet> request;
        if (type) {
            request = mAPIInterface.getPopularMovies();
        } else{
            request = mAPIInterface.getTopRatedMovies();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            /*
             * When you click the reset menu item, we want to start all over
             * and display the pretty gradient again. There are a few similar
             * ways of doing this, with this one being the simplest of those
             * ways. (in our humble opinion)
             */
            case R.id.menu_sort_most_popular:
                populateData(true);
                return true;
            case R.id.menu_sort_top_rated:
                populateData(false);
                return true;

        }
        item.setChecked(true);

        return super.onOptionsItemSelected(item);
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
