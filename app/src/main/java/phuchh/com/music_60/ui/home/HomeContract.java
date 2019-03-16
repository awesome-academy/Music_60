package phuchh.com.music_60.ui.home;

import android.support.v4.app.FragmentManager;

import phuchh.com.music_60.ui.online.OnlineFragment;

public interface HomeContract {
    interface View {
    }

    interface Presenter {
        TabAdapter getTabAdapter(FragmentManager fragmentManager, String online, String local);
    }
}
