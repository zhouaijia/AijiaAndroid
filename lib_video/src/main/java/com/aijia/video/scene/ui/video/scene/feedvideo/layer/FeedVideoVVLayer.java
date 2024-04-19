
package com.aijia.video.scene.ui.video.scene.feedvideo.layer;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoLayerHost;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;
import com.aijia.video.scene.utils.TimeUtils;
import com.aijia.video.scene.utils.UIUtils;

public class FeedVideoVVLayer extends AnimateLayer {

    @Nullable
    @Override
    public String tag() {
        return "feed_video_vv";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = (int) UIUtils.dip2Px(parent.getContext(), 8);
        lp.bottomMargin = (int) UIUtils.dip2Px(parent.getContext(), 6);
        lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        textView.setLayoutParams(lp);
        return textView;
    }


    @Override
    protected void onBindLayerHost(@NonNull VideoLayerHost layerHost) {
        show();
    }

    @Override
    public void onVideoViewBindDataSource(MediaSource dataSource) {
        show();
    }

    @Override
    public void show() {
        super.show();

        VideoView videoView = videoView();
        if (videoView == null) return;

        MediaSource mediaSource = videoView.getDataSource();
        if (mediaSource == null) return;

        TextView textView = getView();
        if (textView != null) {
            textView.setText(String.format("%s%s", textView.getContext().getString(R.string.vevod_feed_video_vv_layer_watch_count_info), TimeUtils.time2String(mediaSource.getDuration())));
        }
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
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
