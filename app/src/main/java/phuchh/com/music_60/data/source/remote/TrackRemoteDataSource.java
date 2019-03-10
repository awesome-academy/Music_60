package phuchh.com.music_60.data.source.remote;

import phuchh.com.music_60.data.source.TrackDataSource;
import phuchh.com.music_60.utils.StringUtil;

public class TrackRemoteDataSource implements TrackDataSource.RemoteDataSource {
    private static TrackRemoteDataSource sInstance;

    private TrackRemoteDataSource() {
    }

    public static TrackRemoteDataSource getInstance() {
        if (sInstance == null) {
            synchronized (TrackRemoteDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TrackRemoteDataSource();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getTracks(String genre, int limit, int offSet,
                          TrackDataSource.FetchTrackCallback callback) {
        new FetchTrack(callback)
                .execute(StringUtil.getUrlTrackByGenre(genre, limit, offSet));
    }

}
