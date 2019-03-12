package phuchh.com.music_60.service;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        ServiceRequest.REQUEST_CREATE,
        ServiceRequest.REQUEST_NEXT,
        ServiceRequest.REQUEST_PREVIOUS,
        ServiceRequest.REQUEST_START,
        ServiceRequest.REQUEST_PAUSE,
        ServiceRequest.REQUEST_SEEK,
        ServiceRequest.REQUEST_PREPARE,
        ServiceRequest.VALUE_NEXT_SONG,
        ServiceRequest.VALUE_PREVIOUS_SONG,
        ServiceRequest.VALUE_PLAY_SONG
})
public @interface ServiceRequest {
    int REQUEST_CREATE = 0;
    int REQUEST_NEXT = 1;
    int REQUEST_PREVIOUS = 2;
    int REQUEST_START = 3;
    int REQUEST_PAUSE = 4;
    int REQUEST_SEEK = 5;
    int REQUEST_PREPARE = 6;
    int VALUE_NEXT_SONG = 8;
    int VALUE_PREVIOUS_SONG = 9;
    int VALUE_PLAY_SONG = 7;
}
