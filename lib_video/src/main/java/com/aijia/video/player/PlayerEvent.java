
package com.aijia.video.player;

import com.aijia.video.player.event.ActionPause;
import com.aijia.video.player.event.ActionPrepare;
import com.aijia.video.player.event.ActionRelease;
import com.aijia.video.player.event.ActionSeekTo;
import com.aijia.video.player.event.ActionSetLooping;
import com.aijia.video.player.event.ActionSetSpeed;
import com.aijia.video.player.event.ActionSetSurface;
import com.aijia.video.player.event.ActionStart;
import com.aijia.video.player.event.ActionStop;
import com.aijia.video.player.event.InfoAudioRenderingStart;
import com.aijia.video.player.event.InfoBufferingEnd;
import com.aijia.video.player.event.InfoBufferingStart;
import com.aijia.video.player.event.InfoBufferingUpdate;
import com.aijia.video.player.event.InfoCacheUpdate;
import com.aijia.video.player.event.InfoDataSourceRefreshed;
import com.aijia.video.player.event.InfoProgressUpdate;
import com.aijia.video.player.event.InfoSeekComplete;
import com.aijia.video.player.event.InfoSeekingStart;
import com.aijia.video.player.event.InfoTrackChanged;
import com.aijia.video.player.event.InfoTrackInfoReady;
import com.aijia.video.player.event.InfoTrackWillChange;
import com.aijia.video.player.event.InfoVideoRenderingStart;
import com.aijia.video.player.event.InfoVideoRenderingStartBeforeStart;
import com.aijia.video.player.event.InfoVideoSARChanged;
import com.aijia.video.player.event.InfoVideoSizeChanged;
import com.aijia.video.player.event.StateCompleted;
import com.aijia.video.player.event.StateError;
import com.aijia.video.player.event.StateIDLE;
import com.aijia.video.player.event.StatePaused;
import com.aijia.video.player.event.StatePrepared;
import com.aijia.video.player.event.StatePreparing;
import com.aijia.video.player.event.StateReleased;
import com.aijia.video.player.event.StateStarted;
import com.aijia.video.player.event.StateStopped;
import com.aijia.video.player.utils.event.Event;

public interface PlayerEvent {

    /**
     * Player action event type constants.
     *
     * @see Event#code()
     */
    class Action {
        /**
         * @see ActionSetSurface
         */
        public static final int SET_SURFACE = 1001;
        /**
         * @see ActionPrepare
         */
        public static final int PREPARE = 1002;
        /**
         * @see ActionStart
         */
        public static final int START = 1003;
        /**
         * @see ActionPause
         */
        public static final int PAUSE = 1004;
        /**
         * @see ActionStop
         */
        public static final int STOP = 1005;
        /**
         * @see ActionRelease
         */
        public static final int RELEASE = 1006;
        /**
         * @see ActionSeekTo
         */
        public static final int SEEK_TO = 1007;
        /**
         * @see ActionSetLooping
         */
        public static final int SET_LOOPING = 1008;
        /**
         * @see ActionSetSpeed
         */
        public static final int SET_SPEED = 1009;
    }

    /**
     * Player state event type constants.
     *
     * @see Event#code()
     */
    class State {
        /**
         * @see StateIDLE
         */
        public static final int IDLE = 2001;
        /**
         * @see StatePreparing
         */
        public static final int PREPARING = 2002;
        /**
         * @see StatePrepared
         */
        public static final int PREPARED = 2003;
        /**
         * @see StateStarted
         */
        public static final int STARTED = 2004;
        /**
         * @see StatePaused
         */
        public static final int PAUSED = 2005;
        /**
         * @see StateStopped
         */
        public static final int STOPPED = 2006;
        /**
         * @see StateReleased
         */
        public static final int RELEASED = 2007;
        /**
         * @see StateCompleted
         */
        public static final int COMPLETED = 2008;
        /**
         * @see StateError
         */
        public static final int ERROR = 2009;
    }

    /**
     * Player info event type constants.
     *
     * @see Event#code()
     */
    class Info {
        /**
         * @see InfoDataSourceRefreshed
         */
        public static final int DATA_SOURCE_REFRESHED = 3001;
        /**
         * @see InfoVideoSizeChanged
         */
        public static final int VIDEO_SIZE_CHANGED = 3002;
        /**
         * @see InfoVideoSARChanged
         */
        public static final int VIDEO_SAR_CHANGED = 3003;
        /**
         * @see InfoVideoRenderingStart
         */
        public static final int VIDEO_RENDERING_START = 3004;
        /**
         * @see InfoAudioRenderingStart
         */
        public static final int AUDIO_RENDERING_START = 3005;

        /**
         * @see InfoVideoRenderingStartBeforeStart
         */
        public static final int VIDEO_RENDERING_START_BEFORE_START = 3006;

        /**
         * @see InfoBufferingStart
         */
        public static final int BUFFERING_START = 3007;
        /**
         * @see InfoBufferingEnd
         */
        public static final int BUFFERING_END = 3008;
        /**
         * @see InfoBufferingUpdate
         */
        public static final int BUFFERING_UPDATE = 3009;
        /**
         * @see InfoSeekingStart
         */
        public static final int SEEKING_START = 3010;
        /**
         * @see InfoSeekComplete
         */
        public static final int SEEK_COMPLETE = 3011;
        /**
         * @see InfoProgressUpdate
         */
        public static final int PROGRESS_UPDATE = 3012;
        /**
         * @see InfoTrackInfoReady
         */
        public static final int TRACK_INFO_READY = 3013;
        /**
         * @see InfoTrackWillChange
         */
        public static final int TRACK_WILL_CHANGE = 3014;
        /**
         * @see InfoTrackChanged
         */
        public static final int TRACK_CHANGED = 3015;
        /**
         * @see InfoCacheUpdate
         */
        public static final int CACHE_UPDATE = 3016;

        public static final int SUBTITLE_STATE_CHANGED = 3017;

        public static final int SUBTITLE_LIST_INFO_READY = 3018;

        public static final int SUBTITLE_FILE_LOAD_FINISH = 3019;

        public static final int SUBTITLE_WILL_CHANGE = 3020;

        public static final int SUBTITLE_TEXT_UPDATE = 3021;

        public static final int SUBTITLE_CHANGED = 3022;

        public static final int FRAME_INFO_UPDATE = 3023;
    }
}
