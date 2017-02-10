package eu.alatar.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);

        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_list);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mMoviesRecyclerView.setLayoutManager(layoutManager);

        mMoviesAdapter = new MoviesAdapter();

        mMoviesRecyclerView.setAdapter(mMoviesAdapter);
    }



}
