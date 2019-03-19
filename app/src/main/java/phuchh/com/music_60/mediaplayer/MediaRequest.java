package phuchh.com.music_60.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MediaRequest.LOADING,
        MediaRequest.SUCCESS,
        MediaRequest.UPDATE_PLAY_ACTIVITY,
        MediaRequest.UPDATE_PLAYER,
        MediaRequest.FAILURE,
        MediaRequest.PAUSED,
        MediaRequest.STOPPED,
        MediaRequest.LOAD_DATA
})
public @interface MediaRequest {
    int LOADING = 1;
    int SUCCESS = 2;
    int UPDATE_PLAY_ACTIVITY = 3;
    int UPDATE_PLAYER = 4;
    int FAILURE = 5;
    int PAUSED = 6;
    int STOPPED = 7;
    int LOAD_DATA = 8;
}
