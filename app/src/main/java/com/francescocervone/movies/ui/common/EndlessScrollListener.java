package com.francescocervone.movies.ui.common;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int mVisibleThreshold = 5;
    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;

    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public EndlessScrollListener(GridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessScrollListener(StaggeredGridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        mVisibleThreshold = mVisibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < mPreviousTotalItemCount) {
            mPreviousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                mLoading = true;
            }
        }

        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        if (!mLoading && (lastVisibleItemPosition + mVisibleThreshold) > totalItemCount) {
            mLoading = true;
            onLoadMore();
        }
    }

    public void resetState() {
        mPreviousTotalItemCount = 0;
        mLoading = true;
    }
    
    public void stopLoading() {
        mLoading = false;
    }

    public abstract void onLoadMore();

}