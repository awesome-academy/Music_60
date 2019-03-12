package phuchh.com.music_60.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import phuchh.com.music_60.data.model.Track;

public class MediaPlayerManager implements PlayMusic,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private static final int CHANGE_POSITION = 1;
    private static MediaPlayerManager sInstance;
    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;
    private int mCurrentIndex;
    private int mState;
    private OnLoadingTrackListener mListener;

    private MediaPlayerManager(Context context) {
        mListener = (OnLoadingTrackListener) context;
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (sInstance == null)
            sInstance = new MediaPlayerManager(context);
        return sInstance;
    }

    @Override
    public void create(int index) {
        mCurrentIndex = index;
        Track track = mTracks.get(mCurrentIndex);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mState = PlayerStatus.IDLE;
        } else {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
        }
        if (!mTracks.isEmpty())
            initOnline(track);
    }

    @Override
    public void prepare() {
        if (mMediaPlayer != null) {
            mState = PlayerStatus.PREPARING;
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
        }
    }

    @Override
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mState = PlayerStatus.STARTED;
            mListener.onLoadingSuccess();
        } else
            create(getTrack());
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mState = PlayerStatus.PAUSED;
            mListener.onTrackPaused();
        }
    }

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mState = PlayerStatus.STOPPED;
            mListener.onTrackStopped();
        }
    }

    @Override
    public int getDuration() {
        if (mMediaPlayer != null && mState >= PlayerStatus.STARTED) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (mMediaPlayer != null && mState >= PlayerStatus.STARTED) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    @Override
    public void seek(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public int getTrack() {
        return mCurrentIndex;
    }

    @Override
    public void setTrack(int index) {
        mCurrentIndex = index;
    }

    @Override
    public void next() {
        mCurrentIndex++;
        mCurrentIndex = mCurrentIndex == mTracks.size() ? 0 : mCurrentIndex;
        create(mCurrentIndex);
        mListener.onChangeTrack();
    }

    @Override
    public void previous() {
        if (mCurrentIndex == 0) {
            mCurrentIndex = getTracks().size() - CHANGE_POSITION;
        } else {
            mCurrentIndex = --mCurrentIndex;
        }
        create(mCurrentIndex);
        mListener.onChangeTrack();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mCurrentIndex == mTracks.size() - CHANGE_POSITION && mState != PlayerStatus.STOPPED) {
            stop();
        } else {
            next();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        start();
        mListener.onChangeTrack();
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public MediaPlayerManager setTracks(List<Track> tracks) {
        mTracks = tracks;
        return this;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }

    private void initOnline(Track track) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(track.getUri());
            mListener.onStartLoading();
            mState = PlayerStatus.INITIALIZED;
            prepare();
        } catch (IOException e) {
            mListener.onLoadingFail(e.getMessage());
        }
    }

    public interface OnLoadingTrackListener {
        void onStartLoading();

        void onLoadingFail(String message);

        void onLoadingSuccess();

        void onChangeTrack();

        void onTrackPaused();

        void onTrackStopped();
    }
}
