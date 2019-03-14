package phuchh.com.music_60.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import phuchh.com.music_60.mediaplayer.MediaRequest;
import phuchh.com.music_60.ui.playmusic.PlayMusicFragment;

public class PlayServiceHandler extends Handler {
    private static WeakReference<Activity> mActivity;
    private static WeakReference<PlayMusicFragment> mFragment;

    public PlayServiceHandler(Activity activity, PlayMusicFragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = mActivity.get();
        PlayMusicFragment fragment = mFragment.get();
        switch (msg.what) {
            case MediaRequest.LOADING:
                if (!activity.isDestroyed()) {
                    fragment.startLoading();
                }
                break;
            case MediaRequest.SUCCESS:
                fragment.loadingSuccess();
                break;
            case MediaRequest.PAUSED:
                fragment.showPlay();
                break;
            case MediaRequest.FAILURE:
                Toast.makeText(activity.getApplicationContext(), (String) msg.obj,
                        Toast.LENGTH_SHORT).show();
                break;
            case MediaRequest.STOPPED:
                fragment.showPlay();
                break;
            case Constant.UPDATE_SEEKBAR:
                fragment.requestUpdateSeekBar();
            default:
                break;
        }
    }
}
