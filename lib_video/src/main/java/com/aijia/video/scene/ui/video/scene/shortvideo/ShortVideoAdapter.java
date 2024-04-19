
package com.aijia.video.scene.ui.video.scene.shortvideo;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aijia.video.player.playback.DisplayModeHelper;
import com.aijia.video.player.playback.DisplayView;
import com.aijia.video.player.playback.VideoLayerHost;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.data.model.VideoItem;
import com.aijia.video.scene.ui.video.layer.LoadingLayer;
import com.aijia.video.scene.ui.video.layer.LogLayer;
import com.aijia.video.scene.ui.video.layer.PauseLayer;
import com.aijia.video.scene.ui.video.layer.PlayErrorLayer;
import com.aijia.video.scene.ui.video.layer.PlayerConfigLayer;
import com.aijia.video.scene.ui.video.scene.PlayScene;
import com.aijia.video.scene.ui.video.scene.shortvideo.layer.ShortVideoCoverLayer;
import com.aijia.video.scene.ui.video.scene.shortvideo.layer.ShortVideoProgressBarLayer;

import java.util.ArrayList;
import java.util.List;


public class ShortVideoAdapter extends RecyclerView.Adapter<ShortVideoAdapter.ViewHolder> {

    private final List<VideoItem> mItems = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<VideoItem> videoItems) {
        mItems.clear();
        mItems.addAll(videoItems);
        notifyDataSetChanged();
    }

    public void prependItems(List<VideoItem> videoItems) {
        if (videoItems != null && !videoItems.isEmpty()) {
            mItems.addAll(0, videoItems);
            notifyItemRangeInserted(0, videoItems.size());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void appendItems(List<VideoItem> videoItems) {
        if (videoItems != null && !videoItems.isEmpty()) {
            int count = mItems.size();
            mItems.addAll(videoItems);
            if (count > 0) {
                notifyItemRangeInserted(count, mItems.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < mItems.size()) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void replaceItem(int position, VideoItem videoItem) {
        if (0 <= position && position < mItems.size()) {
            mItems.set(position, videoItem);
            notifyItemChanged(position);
        }
    }

    public VideoItem getItem(int position) {
        return mItems.get(position);
    }

    public List<VideoItem> getItems() {
        return mItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.videoView.stopPlayback();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.videoView.stopPlayback();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoItem videoItem = mItems.get(position);
        holder.bind(position, videoItem);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final VideoView videoView;

        public static ViewHolder create(ViewGroup parent) {
            VideoView videoView = createVideoView(parent);
            videoView.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
            return new ViewHolder(videoView);
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = (VideoView) itemView;
            videoView.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        }

        public void bind(int position, VideoItem videoItem) {
            MediaSource mediaSource = videoView.getDataSource();
            if (mediaSource == null) {
                mediaSource = VideoItem.toMediaSource(videoItem, false);
                videoView.bindDataSource(mediaSource);
            } else {
                if (!TextUtils.equals(videoItem.getVid(), mediaSource.getMediaId())) {
                    videoView.stopPlayback();
                    mediaSource = VideoItem.toMediaSource(videoItem, false);
                    videoView.bindDataSource(mediaSource);
                } else {
                    // vid is same
                    if (videoView.player() == null) {
                        mediaSource = VideoItem.toMediaSource(videoItem, false);
                        videoView.bindDataSource(mediaSource);
                    } else {
                        // do nothing
                    }
                }
            }
        }
    }

    static VideoView createVideoView(ViewGroup parent) {
        VideoView videoView = new VideoView(parent.getContext());
        VideoLayerHost layerHost = new VideoLayerHost(parent.getContext());
        layerHost.addLayer(new PlayerConfigLayer());
        layerHost.addLayer(new ShortVideoCoverLayer());
        layerHost.addLayer(new LoadingLayer());
        layerHost.addLayer(new PauseLayer());
        layerHost.addLayer(new ShortVideoProgressBarLayer());
        layerHost.addLayer(new PlayErrorLayer());
        if (VideoSettings.booleanValue(VideoSettings.DEBUG_ENABLE_LOG_LAYER)) {
            layerHost.addLayer(new LogLayer());
        }
        layerHost.attachToVideoView(videoView);
        videoView.setBackgroundColor(parent.getResources().getColor(android.R.color.black));
        //videoView.setDisplayMode(DisplayModeHelper.DISPLAY_MODE_ASPECT_FIT); // fit mode
        videoView.setDisplayMode(DisplayModeHelper.DISPLAY_MODE_ASPECT_FILL); // immersive mode
        if (VideoSettings.intValue(VideoSettings.COMMON_RENDER_VIEW_TYPE) == DisplayView.DISPLAY_VIEW_TYPE_TEXTURE_VIEW) {
            // 推荐使用 TextureView, 兼容性更好
            videoView.selectDisplayView(DisplayView.DISPLAY_VIEW_TYPE_TEXTURE_VIEW);
        } else {
            videoView.selectDisplayView(DisplayView.DISPLAY_VIEW_TYPE_SURFACE_VIEW);
        }
        videoView.setPlayScene(PlayScene.SCENE_SHORT);
        return videoView;
    }
}

