package phuchh.com.music_60.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;
import phuchh.com.music_60.ui.playmusic.PlayMusicFragment;

public class HomeActivity extends AppCompatActivity
        implements HomeContract.View {

    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        mTabAdapter = mPresenter.getTabAdapter(getSupportFragmentManager(),
                getString(R.string.title_online), getString(R.string.title_local));
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mPresenter = new HomePresenter(this,
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance()));
        mTabLayout = findViewById(R.id.tab_home);
        mViewPager = findViewById(R.id.pager_home);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_playmusic, PlayMusicFragment.newInstance());
        fragmentTransaction.commit();
    }
}
