package phuchh.com.music_60.ui.online;

import java.util.List;

import phuchh.com.music_60.data.model.Track;

public interface OnlineContract {
    interface View {
        void showFailedMsg(String msg);
        void showTopChart(List<Track> tracks);
    }

    interface Presenter {
        void getTopChart(String genre, int limit, int offset);
    }
}
