
package com.aijia.video.scene.ui.video.scene.feedvideo.layer;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;
import com.aijia.video.scene.utils.UIUtils;

public class FeedVideoCoverShadowLayer extends AnimateLayer {

    @Nullable
    @Override
    public String tag() {
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setBackground(parent.getResources().getDrawable(R.drawable.feed_video_item_cover_bottom_shadow));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) UIUtils.dip2Px(parent.getContext(), 80));
        lp.gravity = Gravity.BOTTOM;
        imageView.setLayoutParams(lp);
        return imageView;
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
    }

    @Override
    public void onVideoViewBindDataSource(MediaSource dataSource) {
        super.onVideoViewBindDataSource(dataSource);
        show();
    }

    private final Dispatcher.EventListener mPlaybackListener = new Dispatcher.EventListener() {

        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlaybackEvent.Action.START_PLAYBACK: {
                    animateDismiss();
                    break;
                }
                case PlaybackEvent.Action.STOP_PLAYBACK: {
                    show();
                    break;
                }
            }
        }
    };
}
