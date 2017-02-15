package eu.alatar.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import eu.alatar.popularmovies.preferences.Preferences;
import eu.alatar.popularmovies.rest.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie mMovie;

    private TextView mMovieTitleTextView;
    private TextView mMovieReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);

        // Parse and display intent context
        mMovie = (Movie) getIntent().getSerializableExtra("movie");
        mMovieTitleTextView.setText(mMovie.getTitle());
        mMovieReleaseDateTextView.setText(mMovie.getReleaseDate());
    }


}
