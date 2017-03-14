package eu.alatar.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eu.alatar.popularmovies.rest.models.Movie;

/**
 * Created by kam on 10/02/17.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movie> mMovies;
    private MovieListClickHandler mClickHandler;

    public interface MovieListClickHandler {
        void onClick(Movie movie);
    }

    public MovieListAdapter(MovieListClickHandler handler) {
        this.mMovies = new ArrayList<Movie>();
        this.mClickHandler = handler;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_list_rv_poster, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        String posterUrl = MovieDetailActivity.API_POSTER_PATH_PREFIX + movie.getPosterPath();
        Context context = holder.mImageViewPoster.getContext();
        /* TODO: what if the poster is not obtained? */
        Picasso.with(context)
                .load(posterUrl)
                .into(holder.mImageViewPoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageViewPoster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mImageViewPoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = mMovies.get(adapterPosition);
            mClickHandler.onClick(currentMovie);
        }
    }

    public void addMovie(Movie movie) {
        this.mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void addMovies(ArrayList<Movie> movies) {
        this.mMovies.clear();
        this.mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getDataParcelable() {
        return this.mMovies;
    }

    public void applyDataParcelable(ArrayList<Movie> movieList) {
        this.mMovies = movieList;
        notifyDataSetChanged();
    }
}
