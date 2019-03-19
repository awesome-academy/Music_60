package phuchh.com.music_60.ui.listtrack;

import java.util.List;

import phuchh.com.music_60.data.model.Track;

public interface ListTrackContract {
    interface View {
        void showFailedMsg(String msg);
        void showTracks(List<Track> tracks);
    }

    interface Presenter {
        void getTracks(String genre, int limit, int offset);
    }
}
