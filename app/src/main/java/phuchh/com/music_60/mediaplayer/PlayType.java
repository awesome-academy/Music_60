package phuchh.com.music_60.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        PlayType.LOOP_ALL,
        PlayType.LOOP_ONE,
        PlayType.SHUFFLE,

})
public @interface PlayType {
    int LOOP_ALL = 1;
    int LOOP_ONE = 2;
    int SHUFFLE = 3;
}
