package phuchh.com.music_60.ui.listtrack;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;
import phuchh.com.music_60.service.PlayMusicService;
import phuchh.com.music_60.ui.home.HomePresenter;
import phuchh.com.music_60.ui.playmusic.PlayMusicFragment;
import phuchh.com.music_60.utils.PlayServiceHandler;

import static phuchh.com.music_60.service.PlayMusicService.getMyServiceIntent;

public class ListTrackActivity extends AppCompatActivity {

    private ServiceConnection mConnection;
    private PlayMusicService mService;

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

    private void initView() {
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
