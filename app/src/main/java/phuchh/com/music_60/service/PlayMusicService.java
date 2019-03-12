package phuchh.com.music_60.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.mediaplayer.MediaPlayerManager;
import phuchh.com.music_60.mediaplayer.MediaRequest;
import phuchh.com.music_60.mediaplayer.PlayMusic;

public class PlayMusicService extends Service implements PlayMusic.Service, Serializable,
        MediaPlayerManager.OnLoadingTrackListener {

    public static final String EXTRA_REQUEST_CODE = " phuchh.com.music_60.service.EXTRA.REQUEST_CODE";
    private static final String WORKER_THREAD_NAME = "ServiceThread";
    private static Handler mUIHandler;
    private ServiceHandler mServiceHandler;
    private final IBinder mBinder = new LocalBinder();
    private MediaPlayerManager mMediaPlayerManager;
    private PendingIntent mNextPendingIntent;
    private PendingIntent mPreviousPendingIntent;
    private PendingIntent mPlayPendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = MediaPlayerManager.getInstance(this);
        HandlerThread thread = new HandlerThread(WORKER_THREAD_NAME);
        thread.start();
        mServiceHandler = new ServiceHandler(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int request = intent.getIntExtra(EXTRA_REQUEST_CODE, 0);
            switch (request) {
                case ServiceRequest.VALUE_NEXT_SONG:
                    nextTrack();
                    break;
                case ServiceRequest.VALUE_PREVIOUS_SONG:
                    previousTrack();
                    break;
                case ServiceRequest.VALUE_PLAY_SONG:
                    if (isPlaying()) {
                        pauseTrack();
                    } else {
                        startTrack();
                    }
                    break;
                default:
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayerManager.setMediaPlayer(null);
    }

    @Override
    public void create(int index) {
        mMediaPlayerManager.create(index);
    }

    @Override
    public void prepare() {
        mMediaPlayerManager.prepare();
    }

    @Override
    public void start() {
        mMediaPlayerManager.start();
    }

    @Override
    public void pause() {
        mMediaPlayerManager.pause();
    }

    @Override
    public void stop() {
        mMediaPlayerManager.stop();
    }

    @Override
    public int getDuration() {
        return mMediaPlayerManager.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayerManager.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayerManager.isPlaying();
    }

    @Override
    public void seek(int position) {
        mMediaPlayerManager.seek(position);
    }

    @Override
    public int getTrack() {
        return mMediaPlayerManager != null ? mMediaPlayerManager.getTrack() : 0;
    }

    @Override
    public void next() {
        mMediaPlayerManager.next();
    }

    @Override
    public void previous() {
        mMediaPlayerManager.previous();
    }

    public static void setUIHandler(Handler uiHandler) {
        mUIHandler = uiHandler;
    }

    @Override
    public void onStartLoading() {
        if (mUIHandler != null)
            mUIHandler.sendEmptyMessage(MediaRequest.LOADING);
        //TODO: update notification
    }

    @Override
    public void onLoadingFail(String message) {
        Message msg = new Message();
        msg.what = MediaRequest.FAILURE;
        msg.obj = message;
        mUIHandler.sendMessage(msg);
    }

    @Override
    public void onLoadingSuccess() {
        mUIHandler.sendEmptyMessage(MediaRequest.SUCCESS);
    }

    @Override
    public void onTrackPaused() {
        mUIHandler.sendEmptyMessage(MediaRequest.PAUSED);
    }

    @Override
    public void onTrackStopped() {
        mUIHandler.sendEmptyMessage(MediaRequest.STOPPED);
    }

    public void onChangeTrack() {
        mUIHandler.sendEmptyMessage(MediaRequest.UPDATE_PLAYER);
    }

    public static Intent getMyServiceIntent(Context context) {
        return new Intent(context, PlayMusicService.class);
    }

    public class LocalBinder extends Binder {
        public PlayMusicService getService() {
            return PlayMusicService.this;
        }
    }

    public void createTrack(int index) {
        sendMessage(ServiceRequest.REQUEST_CREATE, index);
    }

    public void nextTrack() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_NEXT);
    }

    public void previousTrack() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_PREVIOUS);
    }

    public void startTrack() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_START);
    }

    public void pauseTrack() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_PAUSE);
    }

    public void seekTrack(int position) {
        sendMessage(ServiceRequest.REQUEST_SEEK, position);
    }

    public void requestPrepareAsync() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_PREPARE);
    }

    public List<Track> getTracks() {
        return mMediaPlayerManager.getTracks();
    }

    public void setTracks(List<Track> tracks) {
        mMediaPlayerManager.setTracks(tracks);
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mMediaPlayerManager;
    }

    private void sendMessage(int request, int index) {
        Message message = new Message();
        message.what = request;
        message.arg1 = index;
        mServiceHandler.sendMessage(message);
    }

    private void sendPendingIntent(int requestValue) {
        Intent intent = new Intent(getApplicationContext(), PlayMusicService.class);
        intent.putExtra(EXTRA_REQUEST_CODE, requestValue);
        switch (requestValue) {
            case ServiceRequest.VALUE_NEXT_SONG:
                mNextPendingIntent = PendingIntent.getService(getApplicationContext(),
                        requestValue, intent, 0);
                break;
            case ServiceRequest.VALUE_PREVIOUS_SONG:
                mPreviousPendingIntent = PendingIntent.getService(getApplicationContext(),
                        requestValue, intent, 0);
                break;
            case ServiceRequest.VALUE_PLAY_SONG:
                mPlayPendingIntent = PendingIntent.getService(getApplicationContext(),
                        requestValue, intent, 0);
                break;
            default:
                break;
        }
    }

    private static final class ServiceHandler extends Handler {
        private final WeakReference<PlayMusicService> mService;

        private ServiceHandler(PlayMusicService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayMusicService service = mService.get();
            switch (msg.what) {
                case ServiceRequest.REQUEST_CREATE:
                    service.create(msg.arg1);
                    break;
                case ServiceRequest.REQUEST_NEXT:
                    service.next();
                    break;
                case ServiceRequest.REQUEST_PREVIOUS:
                    service.previous();
                    break;
                case ServiceRequest.REQUEST_START:
                    service.start();
                    break;
                case ServiceRequest.REQUEST_PAUSE:
                    service.pause();
                    break;
                case ServiceRequest.REQUEST_SEEK:
                    service.seek(msg.arg1);
                    break;
                case ServiceRequest.REQUEST_PREPARE:
                    service.prepare();
                    break;
                default:
                    break;
            }
        }
    }
}
