
package com.aijia.video.scene.ui.widgets.load;

public interface LoadMoreAble {

    void setLoadMoreEnabled(boolean enabled);

    boolean isLoadMoreEnabled();

    void setOnLoadMoreListener(OnLoadMoreListener listener);

    boolean isLoadingMore();

    void showLoadingMore();

    void dismissLoadingMore();

    void finishLoadingMore();

    interface OnLoadMoreListener {
        void onLoadMore();
    }
}
