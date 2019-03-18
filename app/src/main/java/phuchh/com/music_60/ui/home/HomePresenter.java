package phuchh.com.music_60.ui.home;

import android.support.v4.app.FragmentManager;

import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.ui.local.LocalFragment;
import phuchh.com.music_60.ui.online.OnlineFragment;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;
    private TrackRepository mRepository;
    private OnlineFragment onlineFragment;

    public HomePresenter(HomeContract.View view, TrackRepository trackRepository) {
        mView = view;
        mRepository = trackRepository;
    }

    @Override
    public TabAdapter getTabAdapter(FragmentManager fragmentManager, String online, String local) {
        TabAdapter tabAdapter = new TabAdapter(fragmentManager);
        onlineFragment = OnlineFragment.newInstance();
        tabAdapter.addFragment(onlineFragment, online);
        tabAdapter.addFragment(LocalFragment.newInstance(), local);
        return tabAdapter;
    }

}
