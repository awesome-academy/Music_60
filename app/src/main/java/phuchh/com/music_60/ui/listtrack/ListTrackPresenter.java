package phuchh.com.music_60.ui.listtrack;

import java.util.List;

import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.data.source.TrackDataSource;
import phuchh.com.music_60.data.source.TrackRepository;

public class ListTrackPresenter implements ListTrackContract.Presenter, TrackDataSource.FetchTrackCallback {
    private ListTrackContract.View mView;
    private TrackRepository mRepository;

    public ListTrackPresenter(ListTrackContract.View view, TrackRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void getTracks(String genre, int limit, int offset) {
        mRepository.getTracks(genre, limit, offset, this);
    }

    @Override
    public void onSuccess(List<Track> tracks) {
        mView.showTracks(tracks);
    }

    @Override
    public void onFail(String msg) {
        mView.showFailedMsg(msg);
    }
}
