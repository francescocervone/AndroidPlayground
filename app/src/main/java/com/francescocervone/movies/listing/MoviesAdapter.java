package com.francescocervone.movies.listing;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.francescocervone.movies.R;
import com.francescocervone.movies.databinding.ItemMovieBinding;
import com.francescocervone.movies.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.processors.PublishProcessor;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private static final int LOADER_VIEWHOLDER = 0;
    private static final int MOVIE_VIEWHOLDER = 1;

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    private final List<Movie> mMovies = new ArrayList<>();

    private boolean mLoading;

    private PublishProcessor<String> mMovieClickProcessor = PublishProcessor.create();

    public MoviesAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        switch (viewType) {
            case LOADER_VIEWHOLDER:
                viewHolder = new ViewHolder(mLayoutInflater.inflate(R.layout.item_loader, parent, false));
                break;
            case MOVIE_VIEWHOLDER:
                ItemMovieBinding binding = DataBindingUtil.inflate(mLayoutInflater,
                        R.layout.item_movie, parent, false);
                viewHolder = new MovieViewHolder(binding);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MOVIE_VIEWHOLDER:
                ((MovieViewHolder) holder).bind(mContext, mMovies.get(position));
                ((MovieViewHolder) holder).itemView.setOnClickListener(v -> {
                    mMovieClickProcessor.onNext(mMovies.get(position).getId());
                });
                break;
            case LOADER_VIEWHOLDER:
                if (mLoading) {
                    holder.show();
                } else {
                    holder.hide();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mMovies.size()) {
            return LOADER_VIEWHOLDER;
        }
        return MOVIE_VIEWHOLDER;
    }

    public void clear() {
        int oldSize = mMovies.size();
        mMovies.clear();
        notifyItemRangeRemoved(0, oldSize);
    }

    public void addAll(List<Movie> movies) {
        int oldSize = mMovies.size();
        mMovies.addAll(movies);
        notifyItemRangeInserted(oldSize, movies.size());
    }

    public void setLoading(boolean loading) {
        if (mLoading != loading) {
            mLoading = loading;
            if (mLoading) {
                notifyItemChanged(mMovies.size());
            } else {
                notifyItemChanged(mMovies.size());
            }
        }
    }

    public PublishProcessor<String> observeMovieClicks() {
        return mMovieClickProcessor;
    }

    private static class MovieViewHolder extends ViewHolder {

        private ItemMovieBinding mBinding;

        public MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Context context, Movie movie) {
            mBinding.setMovie(movie);
            Glide.with(context)
                    .load(movie.getBackdropUrl())
                    .centerCrop()
                    .into(mBinding.image);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void show() {
            itemView.setVisibility(View.VISIBLE);
        }

        public void hide() {
            itemView.setVisibility(View.GONE);
        }
    }
}
