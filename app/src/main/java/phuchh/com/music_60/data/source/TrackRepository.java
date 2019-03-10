package phuchh.com.music_60.data.source;

import android.support.annotation.NonNull;

import phuchh.com.music_60.data.source.local.TrackLocalDataSource;
import phuchh.com.music_60.data.source.remote.TrackRemoteDataSource;

public class TrackRepository implements TrackDataSource.RemoteDataSource,
        TrackDataSource.LocalDataSource {
    private static TrackRepository sInstance;
    private TrackRemoteDataSource mTrackRemoteDataSource;
    private TrackLocalDataSource mTracksLocalDataSource;

    private TrackRepository(TrackRemoteDataSource trackRemoteDataSource,
                             TrackLocalDataSource tracksLocalDataSource) {
        mTrackRemoteDataSource = trackRemoteDataSource;
        mTracksLocalDataSource = tracksLocalDataSource;
    }

    public static TrackRepository getInstance(TrackRemoteDataSource trackRemoteDataSource,
                                               TrackLocalDataSource tracksLocalDataSource) {
        if (sInstance == null) {
            synchronized (TrackRepository.class) {
                if (sInstance == null) {
                    sInstance = new TrackRepository(trackRemoteDataSource, tracksLocalDataSource);
                }
            }
        }
        return sInstance;
    }

    @Override
    public void getTracks(String genre, int limit, int offSet,
                          @NonNull TrackDataSource.FetchTrackCallback callback) {
        mTrackRemoteDataSource.getTracks(genre, limit, offSet, callback);
    }
}
