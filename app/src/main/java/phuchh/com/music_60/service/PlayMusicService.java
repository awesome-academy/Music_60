package phuchh.com.music_60.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import java.lang.ref.WeakReference;
import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.mediaplayer.MediaPlayerManager;
import phuchh.com.music_60.mediaplayer.MediaRequest;
import phuchh.com.music_60.mediaplayer.PlayMusic;
import phuchh.com.music_60.ui.home.HomeActivity;

public class PlayMusicService extends Service implements PlayMusic,
        MediaPlayerManager.OnLoadingTrackListener {

    public static final String EXTRA_REQUEST_CODE = "phuchh.com.music_60.service.EXTRA.REQUEST_CODE";
    private static final String WORKER_THREAD_NAME = "ServiceThread";
    private static final int NOTIFICATION_ID = 3011;
    private static final int REQUEST_CODE = 0;
    private static Handler mUIHandler;
    private final IBinder mBinder = new LocalBinder();
    private ServiceHandler mServiceHandler;
    private MediaPlayerManager mMediaPlayerManager;
    private RemoteViews mNotificationLayout;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent mNextPendingIntent;
    private PendingIntent mPreviousPendingIntent;
    private PendingIntent mPlayPendingIntent;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = MediaPlayerManager.getInstance(this);
        HandlerThread thread = new HandlerThread(WORKER_THREAD_NAME);
        thread.start();
        mServiceHandler = new ServiceHandler(this, thread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        mBuilder = null;
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
    public void loop(boolean isLoop) {
        mMediaPlayerManager.loop(isLoop);
    }

    @Override
    public void setPlayType(int playType) {
        mMediaPlayerManager.setPlayType(playType);
    }

    @Override
    public void setTrack(int index) {
        mMediaPlayerManager.setTrack(index);
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

    public void setUIHandler(Handler uiHandler) {
        mUIHandler = uiHandler;
    }

    @Override
    public void onStartLoading() {
        if (mBuilder == null) {
            createNotification();
        } else {
            updateNotification(getTrack());
        }
        if (mUIHandler != null)
            mUIHandler.sendEmptyMessage(MediaRequest.LOADING);
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
        updateNotification();
        mUIHandler.sendEmptyMessage(MediaRequest.SUCCESS);
    }

    @Override
    public void onTrackPaused() {
        updateNotification();
        mUIHandler.sendEmptyMessage(MediaRequest.PAUSED);
    }

    @Override
    public void onTrackStopped() {
        updateNotification();
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

    public void requestPrepare() {
        mServiceHandler.sendEmptyMessage(ServiceRequest.REQUEST_PREPARE);
    }

    public List<Track> getTracks() {
        return mMediaPlayerManager.getTracks();
    }

    public void setTracks(List<Track> tracks) {
        mMediaPlayerManager.setTracks(tracks);
    }

    private void sendMessage(int request, int index) {
        Message message = new Message();
        message.what = request;
        message.arg1 = index;
        mServiceHandler.sendMessage(message);
    }

    private void createNotification() {
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.music_cover)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        Intent nextIntent = new Intent(this, HomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(nextIntent);
        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        initLayoutNotification(getTrack());
        startForeground(NOTIFICATION_ID, mBuilder.build());
        createPendingIntent();
    }

    private void createPendingIntent() {
        createNextPendingIntent();
        createPreviousPendingIntent();
        createPlayPendingIntent();
    }

    private void initLayoutNotification(int index) {
        Track track = getTracks().get(index);
        mNotificationLayout = new RemoteViews(getPackageName(), R.layout.layout_notification);
        mNotificationLayout.setTextViewText(R.id.text_noti_song_name, track.getTitle());
        mNotificationLayout.setImageViewResource(R.id.image_play, R.drawable.ic_noti_pause);
        //TODO update noti image
    }

    private void updateNotification(int index) {
        Track track = getTracks().get(index);
        mNotificationLayout.setTextViewText(R.id.text_noti_song_name, track.getTitle());
        if (isPlaying()) {
            mNotificationLayout.setImageViewResource(R.id.image_play, R.drawable.ic_noti_pause);
            mBuilder.setOngoing(false);
            mBuilder.setContent(mNotificationLayout);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            return;
        }
        mNotificationLayout.setImageViewResource(R.id.image_play, R.drawable.ic_noti_play);
        mBuilder.setOngoing(true);
        mBuilder.setContent(mNotificationLayout);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void updateNotification() {
        if (!isPlaying()) {
            mNotificationLayout.setImageViewResource(R.id.image_play, R.drawable.ic_noti_play);
            mBuilder.setOngoing(false);
            mBuilder.setContent(mNotificationLayout);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            return;
        }
        mNotificationLayout.setImageViewResource(R.id.image_play, R.drawable.ic_noti_pause);
        mBuilder.setContent(mNotificationLayout);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void createNextPendingIntent() {
        sendPendingIntent(ServiceRequest.VALUE_NEXT_SONG);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_next, mNextPendingIntent);
    }

    private void createPreviousPendingIntent() {
        sendPendingIntent(ServiceRequest.VALUE_PREVIOUS_SONG);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_previous, mPreviousPendingIntent);
    }

    private void createPlayPendingIntent() {
        sendPendingIntent(ServiceRequest.VALUE_PLAY_SONG);
        mNotificationLayout.setOnClickPendingIntent(R.id.image_play, mPlayPendingIntent);
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

        private ServiceHandler(PlayMusicService service, Looper looper) {
            super(looper);
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
