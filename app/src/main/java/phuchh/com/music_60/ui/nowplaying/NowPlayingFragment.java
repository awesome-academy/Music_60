package phuchh.com.music_60.ui.nowplaying;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.adapter.NowPlayingAdapter;
import phuchh.com.music_60.data.model.Track;

public class NowPlayingFragment extends BottomSheetDialogFragment {

    private static final String ARGUMENT_TRACK_LIST = "ARGUMENT_TRACK_LIST";
    private static final String ARGUMENT_INDEX = "ARGUMENT_INDEX";
    private RecyclerView mRecyclerNowPlaying;
    private List<Track> mTracks;
    private int mIndex;

    public static NowPlayingFragment newInstance(List<Track> tracks, int index) {
        NowPlayingFragment fragment = new NowPlayingFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARGUMENT_TRACK_LIST, (ArrayList<? extends Parcelable>) tracks);
        args.putInt(ARGUMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args == null) {
            return;
        }
        mIndex = args.getInt(ARGUMENT_INDEX);
        mTracks = args.getParcelableArrayList(ARGUMENT_TRACK_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        showPlaylist();
    }

    private void initView() {
        mRecyclerNowPlaying = getView().findViewById(R.id.recycler_now_playing);
    }

    private void showPlaylist() {
        NowPlayingAdapter adapter = new NowPlayingAdapter(getActivity(), mTracks, mIndex);
        mRecyclerNowPlaying.setAdapter(adapter);
        mRecyclerNowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
