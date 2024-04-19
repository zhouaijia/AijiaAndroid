
package com.aijia.video.scene.ui.video.layer;

import static com.aijia.video.scene.ui.video.layer.Layers.VisibilityRequestReason.REQUEST_DISMISS_REASON_DIALOG_SHOW;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.event.InfoSubtitleTextUpdate;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.source.SubtitleText;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;
import com.aijia.video.scene.ui.video.scene.PlayScene;
import com.aijia.video.scene.utils.UIUtils;

public class SubtitleLayer extends AnimateLayer {

    private TextView mSubText;

    @Nullable
    @Override
    public String tag() {
        return "subtitle";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtitle_layer, parent, false);
        mSubText = view.findViewById(R.id.subtitle);
        return view;
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
                case PlaybackEvent.Action.STOP_PLAYBACK:
                    dismiss();
                    break;
                case PlayerEvent.Info.SUBTITLE_STATE_CHANGED: {
                    applyVisible();
                    break;
                }
                case PlayerEvent.Info.SUBTITLE_TEXT_UPDATE: {
                    applyVisible();
                    InfoSubtitleTextUpdate e = event.cast(InfoSubtitleTextUpdate.class);
                    SubtitleText subtitleText = e.subtitleText;
                    if (subtitleText != null && mSubText != null) {
                        mSubText.setText(subtitleText.getText());
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void show() {
        super.show();
        applyTheme();
    }

    @Override
    public void requestDismiss(@NonNull String reason) {
        if (!TextUtils.equals(reason, REQUEST_DISMISS_REASON_DIALOG_SHOW)) {
            super.requestDismiss(reason);
        }
    }

    public void applyVisible() {
        Player player = player();
        if (player == null) {
            dismiss();
            return;
        }
        if (player.isSubtitleEnabled()) {
            show();
        } else {
            dismiss();
        }
    }

    public void applyTheme() {
        if (playScene() == PlayScene.SCENE_FULLSCREEN) {
            applyFullScreenTheme();
        } else {
            applyHalfScreenTheme();
        }
    }

    @Override
    public void onVideoViewPlaySceneChanged(int fromScene, int toScene) {
        applyTheme();
    }

    private void applyHalfScreenTheme() {
        if (mSubText == null) return;

        mSubText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ((ViewGroup.MarginLayoutParams) mSubText.getLayoutParams()).bottomMargin = (int) UIUtils.dip2Px(context(), 12);
        mSubText.requestLayout();
    }

    private void applyFullScreenTheme() {
        if (mSubText == null) return;

        mSubText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        ((ViewGroup.MarginLayoutParams) mSubText.getLayoutParams()).bottomMargin = (int) UIUtils.dip2Px(context(), 16);
        mSubText.requestLayout();
    }
}
