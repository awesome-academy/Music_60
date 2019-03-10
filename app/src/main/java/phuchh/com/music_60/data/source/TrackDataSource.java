package phuchh.com.music_60.data.source;

import java.util.List;

import phuchh.com.music_60.data.model.Track;

public interface TrackDataSource {

    interface FetchTrackCallback {

        void onSuccess(List<Track> tracks);

        void onFail(String msg);
    }

    interface LocalDataSource {
    }

    interface RemoteDataSource {

        void getTracks(String genre, int limit, int offSet, FetchTrackCallback callback);
    }
}
