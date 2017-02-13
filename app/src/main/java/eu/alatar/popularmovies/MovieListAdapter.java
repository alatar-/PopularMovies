package eu.alatar.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.alatar.popularmovies.rest.models.Movie;

/**
 * Created by kam on 10/02/17.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMovies;

    public MovieListAdapter() {
        this.mMovies = new ArrayList<Movie>();
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.mMoviesTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mMoviesTextView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviesTextView = (TextView) itemView.findViewById(R.id.tv_movie_poster);

        }
    }

    public void addMovie(Movie movie) {
        this.mMovies.add(movie);
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        this.mMovies.addAll(movies);
        notifyDataSetChanged();
    }
}
