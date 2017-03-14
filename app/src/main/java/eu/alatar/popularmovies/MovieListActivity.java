package eu.alatar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import eu.alatar.popularmovies.preferences.Preferences;
import eu.alatar.popularmovies.rest.APIInterface;
import eu.alatar.popularmovies.rest.RestService;
import eu.alatar.popularmovies.rest.models.Movie;
import eu.alatar.popularmovies.rest.models.MovieSet;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListActivity extends AppCompatActivity implements MovieListAdapter.MovieListClickHandler {

    private RecyclerView mMoviesRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private ProgressBar mLoadingIndicator;
    private LinearLayout mErrorMessageBox;

    final private String BUNDLE_SORT_ORDER_KEY = "BUNDLE_SORT_ORDER_KEY";
    final private String BUNDLE_RV_STATE = "BUNDLE_RV_STATE";
    final private String BUNDLE_RV_DATA = "BUNDLE_RV_DATA";

    private int mMovieListCurrentSortOrder;
    private Disposable mAPISubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Preferences.TAG, "MovieListActivity: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageBox = (LinearLayout) findViewById(R.id.box_error_message);

        // Initialize Movie List RecyclerView
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);
        GridLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieListAdapter);

        // Initilize view default setting
        mMovieListCurrentSortOrder = Preferences.MOVIE_LIST_DEFAULT_SORT_ORDER;
        Log.d(Preferences.TAG, "MovieListActivity: Default sort order – " + String.valueOf(mMovieListCurrentSortOrder));

        // Restore the state from previous activity lifecycle
        if (savedInstanceState != null) {
            Log.d(Preferences.TAG, "MovieListActivity: Restoring data from provided bundle.");

            // Restore sort order
            mMovieListCurrentSortOrder = savedInstanceState.getInt(BUNDLE_SORT_ORDER_KEY);

            // Restore RV data
            ArrayList<Movie> movieList = savedInstanceState.getParcelableArrayList(BUNDLE_RV_DATA);
            mMovieListAdapter.applyDataParcelable(movieList);

            // Restore RV position
            Parcelable movieListRVState = savedInstanceState.getParcelable(BUNDLE_RV_STATE);
            mMoviesRecyclerView.getLayoutManager().onRestoreInstanceState(movieListRVState);

        } else {
            populateData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(Preferences.TAG, "MovieListActivity: onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);

        MenuItem sortOderMenuItem = menu.findItem(mMovieListCurrentSortOrder);
        sortOderMenuItem.setChecked(true);

        return true;
    }

    private void populateData() {
        Log.d(Preferences.TAG, "MovieListAcitivity: populateData");

        // Show loading indicator
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageBox.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        RestService service = RestService.getAPIService();
        Observable<MovieSet> request = null;
        if (mMovieListCurrentSortOrder == R.id.action_sort_most_popular) {
            request = service.api.getPopularMovies();
        } else if (mMovieListCurrentSortOrder == R.id.action_sort_top_rated) {
            request = service.api.getTopRatedMovies();
        }
        mAPISubscription = request
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(moviesSet -> {
                if (moviesSet != null) {
                    Log.d(Preferences.TAG, "Request successful. Displaying obtained movie posters...");
                    mMovieListAdapter.addMovies(moviesSet.getResults());
                } else {
                    Log.e(Preferences.TAG, "Body is empty!");
                }
            }, e -> {  // request failed
                Log.e(Preferences.TAG, "Request failed!");
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mErrorMessageBox.setVisibility(View.VISIBLE);
            }, () -> { // request completed
                // Hide loading indicator
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mMoviesRecyclerView.setVisibility(View.VISIBLE);
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(Preferences.TAG, "MovieListAcitivity: onOptionsItemSelected");
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
        Log.d(Preferences.TAG, "MovieListAcitivity: onClick — " + movie.getTitle());

        Context context = getApplicationContext();
        Class<MovieDetailActivity> destinationActivity = MovieDetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);

        intent.putExtra(MovieDetailActivity.BUNDLE_MOVIE_KEY, movie);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(Preferences.TAG, "MovieListAcitivity: onSaveInstanceState — " + String.valueOf(mMovieListCurrentSortOrder));

        ArrayList<Movie> data = mMovieListAdapter.getDataParcelable();
        outState.putInt(BUNDLE_SORT_ORDER_KEY, mMovieListCurrentSortOrder);
        outState.putParcelableArrayList(BUNDLE_RV_DATA, data);
        outState.putParcelable(BUNDLE_RV_STATE, mMoviesRecyclerView.getLayoutManager().onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (mAPISubscription != null && !mAPISubscription.isDisposed()) {
            mAPISubscription.dispose();
        }
        super.onDestroy();
    }
}
