
package com.aijia.video.scene.ui.widgets.load.impl;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.aijia.video.scene.ui.widgets.load.LoadMoreAble;

public class ViewPager2LoadMoreHelper implements LoadMoreAble {

    private final ViewPager2 mViewPager;
    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean mLoadingMore;

    private boolean mLoadMoreEnabled = true;

    public ViewPager2LoadMoreHelper(ViewPager2 viewPager) {
        this.mViewPager = viewPager;
        this.mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                final RecyclerView.Adapter<?> adapter = mViewPager.getAdapter();
                if (adapter == null) return;

                final int count = adapter.getItemCount();
                if (position == count - 2 && !isLoadingMore()) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
    }

    @Override
    public boolean isLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadingMore;
    }

    @Override
    public void showLoadingMore() {
        mLoadingMore = true;
    }

    @Override
    public void dismissLoadingMore() {
        mLoadingMore = false;
    }

    @Override
    public void finishLoadingMore() {
        mLoadingMore = false;
    }
}
