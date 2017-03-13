package eu.alatar.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import eu.alatar.popularmovies.rest.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    private Movie mMovie;

    private TextView mMovieUserRatingTextView;
    private TextView mMovieReleaseDateTextView;
    private TextView mMoviePlotTextView;
    private ImageView mMovieThumbnailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mMovieReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        mMovieUserRatingTextView = (TextView) findViewById(R.id.tv_movie_user_rating);
        mMoviePlotTextView = (TextView) findViewById(R.id.tv_movie_plot);
        mMovieThumbnailImageView = (ImageView) findViewById(R.id.iv_movie_thumbnail);

        // Parse and display intent context
        mMovie = (Movie) getIntent().getSerializableExtra("movie");
        setTitle(mMovie.getTitle());

        mMovieReleaseDateTextView.setText(mMovie.getReleaseDate().substring(0, 4));
        mMovieUserRatingTextView.setText(String.valueOf(mMovie.getVoteAverage()) + "/10");
        mMoviePlotTextView.setText(mMovie.getOverview());

        Context context = mMovieThumbnailImageView.getContext();
        String posterUrl = "http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath(); // Use the same size of the poster as it is already cached by Picasso
        Picasso.with(context)
                .load(posterUrl)
                .into(mMovieThumbnailImageView);
    }


}
