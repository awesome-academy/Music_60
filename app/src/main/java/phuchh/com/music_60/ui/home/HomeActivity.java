package phuchh.com.music_60.ui.home;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;
import phuchh.com.music_60.mediaplayer.MediaRequest;
import phuchh.com.music_60.service.PlayMusicService;
import phuchh.com.music_60.ui.playmusic.PlayMusicFragment;
import phuchh.com.music_60.utils.Constant;
import phuchh.com.music_60.utils.PlayServiceHandler;

import static phuchh.com.music_60.service.PlayMusicService.getMyServiceIntent;

public class HomeActivity extends AppCompatActivity
        implements HomeContract.View {

    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeContract.Presenter mPresenter;
    private ServiceConnection mConnection;
    private PlayMusicService mService;
    private Handler mHandler;
    private PlayMusicFragment mPlayFragment;
    private SlidingUpPanelLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setUpTabLayout();
        initHandler();
        bindToService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        if (!mService.isPlaying()) {
            stopService(getMyServiceIntent(this));
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpTabLayout() {
        mTabAdapter = mPresenter.getTabAdapter(getSupportFragmentManager(),
                getString(R.string.title_online), getString(R.string.title_local));
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mLayout = findViewById(R.id.activity_home);
        mPresenter = new HomePresenter(this,
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance()));
        mTabLayout = findViewById(R.id.tab_home);
        mViewPager = findViewById(R.id.pager_home);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mPlayFragment = PlayMusicFragment.newInstance();
        fragmentTransaction.add(R.id.fragment_playmusic, mPlayFragment);
        fragmentTransaction.commit();
    }

    private void bindToService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) iBinder;
                mService = binder.getService();
                mService.setUIHandler(mHandler);
                mPlayFragment.setHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        bindService(getMyServiceIntent(this), mConnection, BIND_AUTO_CREATE);
    }

    private void initHandler() {
        mHandler = new PlayServiceHandler(this, mPlayFragment);
    }

}
