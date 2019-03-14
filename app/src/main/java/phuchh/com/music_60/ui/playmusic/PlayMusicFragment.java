package phuchh.com.music_60.ui.playmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.service.PlayMusicService;
import phuchh.com.music_60.utils.Constant;
import phuchh.com.music_60.utils.StringUtil;

import static phuchh.com.music_60.service.PlayMusicService.getMyServiceIntent;

public class PlayMusicFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener  {

    private static final int START_POSITION = 0;
    private static final long MESSAGE_UPDATE_DELAY = 1000;
    private ImageView mImageBarCover;
    private ImageView mImageMainCover;
    private TextView mTextArtist;
    private TextView mTextSong;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonNext;
    private ImageButton mButtonMainPlay;
    private ImageButton mButtonMainPause;
    private ImageButton mButtonBarPlay;
    private ImageButton mButtomBarPause;
    private SeekBar mSeekBar;
    private TextView mTextCurrentPosition;
    private TextView mTextDuration;
    private Track mTrack;
    private Handler mHandler;
    private PlayMusicService mService;
    private ServiceConnection mConnection;

    public static PlayMusicFragment newInstance() {
        return new PlayMusicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToService();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mService.seekTrack(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_pause:
                onPauseClicked();
                break;
            case R.id.button_main_play:
                onPlayClicked();
                break;
            case R.id.button_next:
                onNextClicked();
                break;
            case R.id.button_previous:
                onPreviousClicked();
                break;
            case R.id.button_toolbar_pause:
                onPauseClicked();
                break;
            case R.id.button_toolbar_play:
                onPlayClicked();
                break;
            default:
                break;
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void startLoading() {
        mSeekBar.setEnabled(false);
        mButtonMainPlay.setClickable(false);
        mButtonBarPlay.setClickable(false);
        mButtonNext.setClickable(false);
        mButtonPrevious.setClickable(false);
        mTrack = mService.getTracks().get(mService.getTrack());
        mTextSong.setText(mTrack.getTitle());
        mSeekBar.setProgress(START_POSITION);
        mTextCurrentPosition.setText(StringUtil.convertMilisecondToFormatTime(START_POSITION));
        mTextDuration.setText(StringUtil.convertMilisecondToFormatTime(START_POSITION));
        setMainCover();
        setBarCover();
    }

    private void setBarCover() {
        if (mTrack.getArtworkUrl().equals(Constant.NULL_RESULT)) {
            Glide.with(this)
                    .load(R.drawable.music_cover)
                    .into(mImageBarCover);
            return;
        }
        Glide.with(this)
                .load(mTrack.getArtworkUrl())
                .into(mImageBarCover);
    }

    private void setMainCover() {
        if (mTrack.getArtworkUrl().equals(Constant.NULL_RESULT)) {
            Glide.with(this)
                    .load(R.drawable.music_cover)
                    .into(mImageMainCover);
            return;
        }
        Glide.with(this)
                .load(mTrack.getArtworkUrl())
                .into(mImageMainCover);
    }

    public void loadingSuccess() {
        mSeekBar.setEnabled(true);
        int duration = mService.getDuration();
        mButtonBarPlay.setClickable(true);
        mButtonMainPlay.setClickable(true);
        mButtonPrevious.setClickable(true);
        mButtonNext.setClickable(true);
        showPause();
        mSeekBar.setMax(duration);
        mTextDuration.setText(StringUtil.convertMilisecondToFormatTime(duration));
        mHandler.sendEmptyMessage(Constant.UPDATE_SEEKBAR);
    }

    public void requestUpdateSeekBar() {
        updateSeekBar();
    }

    public void showPlay() {
        mButtonMainPause.setVisibility(View.INVISIBLE);
        mButtonMainPlay.setVisibility(View.VISIBLE);
        mButtomBarPause.setVisibility(View.INVISIBLE);
        mButtonBarPlay.setVisibility(View.VISIBLE);
    }

    private void showPause() {
        mButtonMainPlay.setVisibility(View.INVISIBLE);
        mButtonMainPause.setVisibility(View.VISIBLE);
        mButtonBarPlay.setVisibility(View.INVISIBLE);
        mButtomBarPause.setVisibility(View.VISIBLE);
    }

    private void initView() {
        mImageBarCover = getView().findViewById(R.id.image_toolbar_cover);
        mImageMainCover = getView().findViewById(R.id.image_main_cover);
        mTextArtist = getView().findViewById(R.id.text_artist_name);
        mTextSong = getView().findViewById(R.id.text_song_title);
        mButtonPrevious = getView().findViewById(R.id.button_previous);
        mButtonNext = getView().findViewById(R.id.button_next);
        mButtonMainPlay = getView().findViewById(R.id.button_main_play);
        mButtonMainPause = getView().findViewById(R.id.button_main_pause);
        mButtonBarPlay = getView().findViewById(R.id.button_toolbar_play);
        mButtomBarPause = getView().findViewById(R.id.button_toolbar_pause);
        mSeekBar = getView().findViewById(R.id.seekbar_duration);
        mTextCurrentPosition = getView().findViewById(R.id.text_start_time);
        mTextDuration = getView().findViewById(R.id.text_end_time);
        setListener();
    }

    private void updateSeekBar() {
        int currentPosition = mService.getCurrentPosition();
        mSeekBar.setProgress(currentPosition);
        mTextCurrentPosition.setText(StringUtil.convertMilisecondToFormatTime(currentPosition));
        mHandler.sendEmptyMessageDelayed(Constant.UPDATE_SEEKBAR, MESSAGE_UPDATE_DELAY);
    }

    private void setListener() {
        mSeekBar.setOnSeekBarChangeListener(this);
        mButtonMainPlay.setOnClickListener(this);
        mButtonMainPause.setOnClickListener(this);
        mButtomBarPause.setOnClickListener(this);
        mButtonBarPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
    }

    private void onPauseClicked() {
        mService.pauseTrack();
        showPlay();
    }

    private void onPlayClicked() {
        mService.startTrack();
        showPause();
    }

    private void onNextClicked() {
        mService.nextTrack();
    }

    private void onPreviousClicked() {
        mService.previousTrack();
    }

    private void bindToService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
                mService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                getActivity().unbindService(mConnection);
            }
        };
        getActivity().bindService(getMyServiceIntent(getActivity()),
                mConnection, Context.BIND_AUTO_CREATE);
    }
}
