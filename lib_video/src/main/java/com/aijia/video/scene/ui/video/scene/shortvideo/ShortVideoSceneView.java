
package com.aijia.video.scene.ui.video.scene.shortvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aijia.video.R;
import com.aijia.video.scene.ui.widgets.load.LoadMoreAble;
import com.aijia.video.scene.ui.widgets.load.RefreshAble;
import com.aijia.video.scene.ui.widgets.load.impl.ViewPager2LoadMoreHelper;


public class ShortVideoSceneView extends FrameLayout implements RefreshAble, LoadMoreAble {

    private final SwipeRefreshLayout mRefreshLayout;

    private final ViewPager2LoadMoreHelper mLoadMoreHelper;
    private final ShortVideoPageView mPageView;
    private final ContentLoadingProgressBar mLoadMoreProgressBar;

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    public ShortVideoSceneView(@NonNull Context context) {
        this(context, null);
    }

    public ShortVideoSceneView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShortVideoSceneView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPageView = new ShortVideoPageView(context);
        // refresh
        mRefreshLayout = new SwipeRefreshLayout(context);
        mRefreshLayout.setOnRefreshListener(() -> {
            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        });
        mRefreshLayout.addView(mPageView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mRefreshLayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        // load more
        mLoadMoreHelper = new ViewPager2LoadMoreHelper(mPageView.viewPager());
        mLoadMoreHelper.setOnLoadMoreListener(() -> {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        });
        mLoadMoreProgressBar = (ContentLoadingProgressBar) LayoutInflater.from(context)
                .inflate(R.layout.short_video_loading_more, this, false);
        mLoadMoreProgressBar.setVisibility(GONE);
        addView(mLoadMoreProgressBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM));
    }

    @Override
    public void setRefreshEnabled(boolean enabled) {
        mRefreshLayout.setEnabled(enabled);
    }

    @Override
    public boolean isRefreshEnabled() {
        return mRefreshLayout.isEnabled();
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    @Override
    public boolean isRefreshing() {
        return mRefreshLayout.isRefreshing();
    }

    @Override
    public void showRefreshing() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissRefreshing() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreHelper.setLoadMoreEnabled(enabled);
    }

    @Override
    public boolean isLoadMoreEnabled() {
        return mLoadMoreHelper.isLoadMoreEnabled();
    }

    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadMoreHelper.isLoadingMore();
    }

    @Override
    public void showLoadingMore() {
        mLoadMoreHelper.showLoadingMore();
        mLoadMoreProgressBar.setVisibility(VISIBLE);
    }

    @Override
    public void dismissLoadingMore() {
        mLoadMoreHelper.dismissLoadingMore();
        mLoadMoreProgressBar.setVisibility(GONE);
    }

    @Override
    public void finishLoadingMore() {
        mLoadMoreHelper.dismissLoadingMore();
        mLoadMoreProgressBar.setVisibility(GONE);
    }

    public ShortVideoPageView pageView() {
        return mPageView;
    }
}
