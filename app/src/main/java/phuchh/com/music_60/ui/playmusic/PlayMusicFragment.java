package phuchh.com.music_60.ui.playmusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.adapter.NowPlayingAdapter;
import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.mediaplayer.PlayType;
import phuchh.com.music_60.service.PlayMusicService;
import phuchh.com.music_60.ui.nowplaying.NowPlayingFragment;
import phuchh.com.music_60.utils.Constant;
import phuchh.com.music_60.utils.StringUtil;

import static phuchh.com.music_60.service.PlayMusicService.getMyServiceIntent;

public class PlayMusicFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, NowPlayingAdapter.NowPlayingOnClickListener,
        NowPlayingAdapter.NowPlayingOnChangeListener {

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
    private ImageButton mButtonBarPause;
    private ImageButton mButtonShuffle;
    private ImageButton mButtonLoopAll;
    private ImageButton mButtonLoopOne;
    private ImageButton mButtonPlaylist;
    private SeekBar mSeekBar;
    private TextView mTextCurrentPosition;
    private TextView mTextDuration;
    private Track mTrack;
    private Handler mHandler;
    private PlayMusicService mService;
    private ServiceConnection mConnection;
    private NowPlayingFragment mNowPlayingFragment;

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
        boundToService();
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
            case R.id.button_loop_all:
                onLoopAllClicked();
                break;
            case R.id.button_loop_one:
                onLoopOneClicked();
                break;
            case R.id.button_shuffle:
                onShuffleClicked();
                break;
            case R.id.button_toolbar_playlist:
                onPlaylistClicked();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNowPlayingClick(int position) {
        mService.createTrack(position);
        mNowPlayingFragment.dismiss();
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
        showPause();
    }

    public void loadData(){
        mTrack = mService.getTracks().get(mService.getTrack());
        mTextSong.setText(mTrack.getTitle());
        mSeekBar.setProgress(START_POSITION);
        mTextCurrentPosition.setText(StringUtil.convertMilisecondToFormatTime(START_POSITION));
        mTextDuration.setText(StringUtil.convertMilisecondToFormatTime(START_POSITION));
        setMainCover();
        setBarCover();
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
        mButtonBarPause.setVisibility(View.INVISIBLE);
        mButtonBarPlay.setVisibility(View.VISIBLE);
    }

    private void showPause() {
        mButtonMainPlay.setVisibility(View.INVISIBLE);
        mButtonMainPause.setVisibility(View.VISIBLE);
        mButtonBarPlay.setVisibility(View.INVISIBLE);
        mButtonBarPause.setVisibility(View.VISIBLE);
    }

    private void setBarCover() {
        Glide.with(this)
                .load(mTrack.getArtworkUrl())
                .placeholder(R.drawable.music_cover)
                .into(mImageBarCover);
    }

    private void setMainCover() {
        Glide.with(this)
                .load(mTrack.getArtworkUrl())
                .placeholder(R.drawable.music_cover)
                .into(mImageMainCover);
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
        mButtonBarPause = getView().findViewById(R.id.button_toolbar_pause);
        mSeekBar = getView().findViewById(R.id.seekbar_duration);
        mTextCurrentPosition = getView().findViewById(R.id.text_start_time);
        mTextDuration = getView().findViewById(R.id.text_end_time);
        mButtonShuffle = getView().findViewById(R.id.button_shuffle);
        mButtonLoopAll = getView().findViewById(R.id.button_loop_all);
        mButtonLoopOne = getView().findViewById(R.id.button_loop_one);
        mButtonPlaylist = getView().findViewById(R.id.button_toolbar_playlist);
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
        mButtonBarPause.setOnClickListener(this);
        mButtonBarPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonLoopAll.setOnClickListener(this);
        mButtonLoopOne.setOnClickListener(this);
        mButtonShuffle.setOnClickListener(this);
        mButtonPlaylist.setOnClickListener(this);
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

    private void onLoopAllClicked() {
        mButtonLoopAll.setVisibility(View.INVISIBLE);
        mButtonLoopOne.setVisibility(View.VISIBLE);
        mService.setPlayType(PlayType.LOOP_ONE);
        mService.loop(true);
    }

    private void onLoopOneClicked() {
        mButtonLoopOne.setVisibility(View.INVISIBLE);
        mButtonShuffle.setVisibility(View.VISIBLE);
        mService.setPlayType(PlayType.SHUFFLE);
        mService.loop(false);
    }

    private void onShuffleClicked() {
        mButtonShuffle.setVisibility(View.INVISIBLE);
        mButtonLoopAll.setVisibility(View.VISIBLE);
        mService.setPlayType(PlayType.LOOP_ALL);
        mService.loop(false);
    }

    private void boundToService() {
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

    private void onPlaylistClicked() {
        if (mService.getTracks() == null) {
            Toast.makeText(getActivity(), getString(R.string.msg_empty_list), Toast.LENGTH_SHORT).show();
            return;
        }
        mNowPlayingFragment = NowPlayingFragment.newInstance(mService.getTracks(), mService.getTrack());
        mNowPlayingFragment.setOnClickListener(this);
        mNowPlayingFragment.setOnChangeListener(this);
        mNowPlayingFragment.show(getFragmentManager(), mNowPlayingFragment.getTag());
    }

    @Override
    public void onNowPlayingMove(List<Track> tracks, int from, int to) {
        mService.setTracks(tracks);
        if (from < mService.getTrack() && to == mService.getTrack()) {
            int index = to - 1;
            mService.setTrack(index);
        } else if (from == mService.getTrack()) {
            mService.setTrack(to);
        } else if (from > mService.getTrack() && to == mService.getTrack()) {
            int index = to + 1;
            mService.setTrack(index);
        }
    }

    @Override
    public void onNowPlayingRemove(List<Track> tracks, int position) {
        mService.setTracks(tracks);
        if (position == mService.getTrack()) {
            mService.createTrack(position);
            mNowPlayingFragment.dismiss();
        }
    }
}
