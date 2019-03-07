package phuchh.com.music_60.ui.playmusic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import phuchh.com.music_60.R;

public class PlayMusicFragment extends Fragment {

    public static PlayMusicFragment newInstance() {
        return new PlayMusicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music, container, false);
    }

}
