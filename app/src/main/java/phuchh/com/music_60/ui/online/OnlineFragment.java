package phuchh.com.music_60.ui.online;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.adapter.GenreAdapter;
import phuchh.com.music_60.adapter.TopchartAdapter;
import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.data.source.TrackRepository;
import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;
import phuchh.com.music_60.utils.Constant;

public class OnlineFragment extends Fragment implements OnlineContract.View,
        TopchartAdapter.TopchartOnClickListener, GenreAdapter.GenreOnClickListener {
    public static final int SPAN_COUNT = 3;
    private OnlineContract.Presenter mPresenter;
    private RecyclerView mRecyclerLatest;
    private RecyclerView mRecyclerGenre;

    public static OnlineFragment newInstance() {
        return new OnlineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFragment();
        mPresenter.getTopChart(Constant.ALL_MUSIC, Constant.LIMIT_DEFAULT, Constant.OFFSET_DEFAULT);
        showGenres();
    }

    @Override
    public void showFailedMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTopChart(List<Track> tracks) {
        TopchartAdapter adapter = new TopchartAdapter(getActivity(), tracks);
        adapter.setTopchartListener(this);
        mRecyclerLatest.setAdapter(adapter);
        mRecyclerLatest.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
    }


    @Override
    public void onGenreClick() {
        //TODO: Update genre click listener;
    }

    @Override
    public void onTopchartClick() {
        //TODO: update play music
    }

    private void initFragment() {
        mRecyclerLatest = getView().findViewById(R.id.recycler_latest);
        mRecyclerGenre = getView().findViewById(R.id.recycler_genres);
        mPresenter = new OnlinePresenter(this,
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance()));
    }

    private void showGenres() {
        GenreAdapter adapter = new GenreAdapter(getActivity());
        adapter.setGenreListener(this);
        mRecyclerGenre.setAdapter(adapter);
        mRecyclerGenre.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
    }
}
