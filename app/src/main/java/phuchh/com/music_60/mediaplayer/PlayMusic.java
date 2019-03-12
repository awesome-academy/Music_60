package phuchh.com.music_60.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface PlayMusic {
    void create(int index);

    void prepare();

    void start();

    void pause();

    void stop();

    int getDuration();

    int getCurrentPosition();

    boolean isPlaying();

    void seek(int possition);

    int getTrack();

    void next();

    void previous();

    interface Media extends PlayMusic{
        void setTrack(int index);
    }

    interface Service extends PlayMusic{
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PlayerStatus.IDLE,
            PlayerStatus.INITIALIZED,
            PlayerStatus.PREPARING,
            PlayerStatus.STARTED,
            PlayerStatus.PAUSED,
            PlayerStatus.STOPPED,
            PlayerStatus.END,
            PlayerStatus.PLAYBACK_COMPLETED})
    @interface PlayerStatus {
        int IDLE = 0;
        int INITIALIZED = 1;
        int PREPARING = 2;
        int STARTED = 3;
        int PAUSED = 4;
        int STOPPED = 5;
        int END = 6;
        int PLAYBACK_COMPLETED = 7;
    }
}
