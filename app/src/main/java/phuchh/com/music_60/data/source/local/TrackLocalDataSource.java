package phuchh.com.music_60.data.source.local;

import phuchh.com.music_60.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.LocalDataSource {
    private static TrackLocalDataSource sInstance;

    private TrackLocalDataSource() {
    }

    public static TrackLocalDataSource getInstance() {
        if (sInstance == null) {
            synchronized (TrackLocalDataSource.class) {
                if (sInstance == null) {
                    sInstance = new TrackLocalDataSource();
                }
            }
        }
        return sInstance;
    }
}
