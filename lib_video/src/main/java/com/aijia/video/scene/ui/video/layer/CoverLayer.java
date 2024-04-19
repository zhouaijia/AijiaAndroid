
package com.aijia.video.scene.ui.video.layer;

import static com.aijia.video.player.playback.DisplayModeHelper.calDisplayAspectRatio;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.DisplayModeHelper;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.scene.ui.video.layer.base.BaseLayer;

public class CoverLayer extends BaseLayer {

    private final DisplayModeHelper mDisplayModeHelper = new DisplayModeHelper();

    @Override
    public String tag() {
        return "cover";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundColor(parent.getResources().getColor(android.R.color.black));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        imageView.setLayoutParams(lp);
        mDisplayModeHelper.setDisplayView(imageView);
        mDisplayModeHelper.setContainerView((FrameLayout) parent);
        return imageView;
    }

    @Override
    public void onVideoViewBindDataSource(MediaSource dataSource) {
        show();
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
                    final Player player = player();
                    if (player != null && player.isInPlaybackState()) {
                        return;
                    }
                    show();
                    break;
                }
                case PlaybackEvent.Action.STOP_PLAYBACK: {
                    show();
                    break;
                }
                case PlayerEvent.Action.SET_SURFACE: {
                    final Player player = player();
                    if (player != null && player.isInPlaybackState()) {
                        dismiss();
                    } else {
                        show();
                    }
                    break;
                }
                case PlayerEvent.Action.START: {
                    final Player player = player();
                    if (player != null && player.isPaused()) {
                        dismiss();
                    }
                    break;
                }
                case PlayerEvent.Action.STOP:
                case PlayerEvent.Action.RELEASE: {
                    show();
                    break;
                }
                case PlayerEvent.Info.VIDEO_RENDERING_START: {
                    dismiss();
                    break;
                }
            }
        }
    };

    @Override
    public void show() {
        super.show();
        load();
    }

    protected void load() {
        final ImageView imageView = getView();
        if (imageView == null) return;
        final String coverUrl = resolveCoverUrl();
        Activity activity = activity();
        if (activity != null && !activity.isDestroyed()) {
            Glide.with(imageView).load(coverUrl).listener(mGlideListener).into(imageView);
        }
    }

    private final RequestListener<Drawable> mGlideListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            mDisplayModeHelper.setDisplayAspectRatio(calDisplayAspectRatio(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), 0));
            VideoView videoView = videoView();
            if (videoView != null) {
                mDisplayModeHelper.setDisplayMode(videoView.getDisplayMode());
            }
            return false;
        }
    };


    String resolveCoverUrl() {
        final VideoView videoView = videoView();
        if (videoView == null) return null;

        final MediaSource mediaSource = videoView.getDataSource();
        if (mediaSource == null) return null;

        return mediaSource.getCoverUrl();
    }
}
