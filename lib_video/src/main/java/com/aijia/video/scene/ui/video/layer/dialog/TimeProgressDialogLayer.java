
package com.aijia.video.scene.ui.video.layer.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aijia.video.player.Player;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.Layers;
import com.aijia.video.scene.ui.video.layer.base.DialogLayer;
import com.aijia.video.scene.ui.video.scene.PlayScene;
import com.aijia.video.scene.utils.TimeUtils;

public class TimeProgressDialogLayer extends DialogLayer {

    private ProgressBar mProgressBar;
    private TextView mTime;

    private long mCurrentPosition;

    @Override
    public String tag() {
        return "time_progress_dialog";
    }

    @Override
    protected View createDialogView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_progress_dialog_layer, parent, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mTime = view.findViewById(R.id.time);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateDismiss();
            }
        });
        return view;
    }

    @Override
    protected int backPressedPriority() {
        return Layers.BackPriority.TIME_PROGRESS_DIALOG_LAYER_PRIORITY;
    }

    @Override
    public void onVideoViewPlaySceneChanged(int fromScene, int toScene) {
        if (playScene() != PlayScene.SCENE_FULLSCREEN) {
            dismiss();
        }
    }

    public void setCurrentPosition(long currentPosition, long duration) {
        mCurrentPosition = currentPosition;
        if (isShowing()) {
            int progress = (int) (currentPosition / (float) duration * 100);
            mProgressBar.setProgress(progress);
            mTime.setText(String.format("%s / %s", TimeUtils.time2String(currentPosition), TimeUtils.time2String(duration)));
        }
    }

    private void syncPosition() {
        Player player = player();
        if (player == null || !player.isInPlaybackState()) return;
        if (mCurrentPosition == 0) {
            mCurrentPosition = player.getCurrentPosition();
        }
        setCurrentPosition(mCurrentPosition, player.getDuration());
    }

    @Override
    public void show() {
        super.show();
        syncPosition();
    }

    public long getCurrentPosition() {

        return mCurrentPosition;
    }
}
