package eu.alatar.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kam on 10/02/17.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MoviesAdapterViewHolder> {

    private String[] mData;

    public MovieListAdapter() {
        this.mData = new String[]{"El1", "Element2", "eli3"};
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String text = mData[position];
        holder.mMoviesTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.length;
        }
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mMoviesTextView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviesTextView = (TextView) itemView.findViewById(R.id.tv_movie_poster);

        }
    }
}
