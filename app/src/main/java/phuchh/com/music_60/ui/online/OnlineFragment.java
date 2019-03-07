package phuchh.com.music_60.ui.online;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import phuchh.com.music_60.R;

public class OnlineFragment extends Fragment {

    public static OnlineFragment newInstance() {
        OnlineFragment onlineFragment = new OnlineFragment();
        return onlineFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container, false);
    }
}
