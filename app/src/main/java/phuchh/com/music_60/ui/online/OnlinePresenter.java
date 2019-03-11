package phuchh.com.music_60.ui.online;

import java.util.List;

import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.data.source.TrackDataSource;
import phuchh.com.music_60.data.source.TrackRepository;

public class OnlinePresenter implements OnlineContract.Presenter, TrackDataSource.FetchTrackCallback {
    private OnlineContract.View mView;
    private TrackRepository mRepository;

    public OnlinePresenter(OnlineContract.View view, TrackRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void getTopChart(String genre, int limit, int offset) {
        mRepository.getTracks(genre, limit, offset, this);
    }

    @Override
    public void onSuccess(List<Track> tracks) {
        mView.showTopChart(tracks);
    }

    @Override
    public void onFail(String msg) {
        mView.showFailedMsg(msg);
    }
}
