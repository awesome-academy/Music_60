package phuchh.com.music_60.ui.listtrack;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.adapter.TrackAdapter;
import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;
import phuchh.com.music_60.service.PlayMusicService;
import phuchh.com.music_60.utils.Constant;

import static phuchh.com.music_60.service.PlayMusicService.getMyServiceIntent;

public class ListTrackActivity extends AppCompatActivity implements ListTrackContract.View,
        TrackAdapter.ListTrackOnClickListener {

    private ListTrackContract.Presenter mPresenter;
    private ServiceConnection mConnection;
    private PlayMusicService mService;
    private RecyclerView mRecyclerTracks;
    private List<Track> mTracks;
    private int mGenreIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_track);
        initView();
        bindToService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    public void showFailedMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTracks(List<Track> tracks) {
        mTracks = tracks;
        TrackAdapter adapter = new TrackAdapter(this, mTracks);
        adapter.setListTrackListener(this);
        mRecyclerTracks.setAdapter(adapter);
    }

    @Override
    public void onListTrackClick(int position) {

    }

    private void initView() {
        mRecyclerTracks = findViewById(R.id.recycler_track_genre);
        mGenreIndex = getIntent().getIntExtra(Constant.EXTRA_GENRE, 0);
        int genreImage = Constant.GENRE_COVERS_DRAWABLE[mGenreIndex];
        ImageView imageGenre = findViewById(R.id.image_track_genre);
        Glide.with(this).load(genreImage).into(imageGenre);
        mPresenter = new ListTrackPresenter(this,
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance()));
        mPresenter.getTracks(Constant.MUSIC_GENRES[mGenreIndex],
                Constant.LIMIT_DEFAULT, Constant.OFFSET_DEFAULT);
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
            }
        };
        bindService(getMyServiceIntent(this), mConnection, BIND_ADJUST_WITH_ACTIVITY);
    }
}
