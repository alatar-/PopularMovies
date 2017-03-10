package eu.alatar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

    final private String BUNDLE_SORT_ORDER_KEY = "sort_order";
    private int mMovieListCurrentSortOrder;

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

        // Initilize view default setting
        mMovieListCurrentSortOrder = Preferences.MOVIE_LIST_DEFAULT_SORT_ORDER;

        // Initilize API interface
        mAPIInterface = RestService.getAPIService().mAPIInterface;

        // Restore the state from previous activity lifecycle
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_SORT_ORDER_KEY)) {
                mMovieListCurrentSortOrder = savedInstanceState.getInt(BUNDLE_SORT_ORDER_KEY);
            }
        }

        // Populate data and set
        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);

        Log.e(Preferences.TAG, String.valueOf(R.id.action_sort_most_popular));
        Log.e(Preferences.TAG, String.valueOf(mMovieListCurrentSortOrder));

        MenuItem sortOderMenuItem = menu.findItem(mMovieListCurrentSortOrder);
        sortOderMenuItem.setChecked(true);

        return true;
    }

    private void populateData() {
        Log.d(Preferences.TAG, "Requesting movie data from API...");
        Observable<MovieSet> request = null;
        if (mMovieListCurrentSortOrder == R.id.action_sort_most_popular) {
            request = mAPIInterface.getPopularMovies();
        } else if (mMovieListCurrentSortOrder == R.id.action_sort_top_rated) {
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

        if (itemId == R.id.action_sort_most_popular || itemId == R.id.action_sort_top_rated) {
            item.setChecked(true);
            mMovieListCurrentSortOrder = itemId;
            populateData();
            return true;
        }

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(BUNDLE_SORT_ORDER_KEY, mMovieListCurrentSortOrder);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
