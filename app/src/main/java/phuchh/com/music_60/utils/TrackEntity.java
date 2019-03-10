package phuchh.com.music_60.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        TrackEntity.COLLECTION,
        TrackEntity.ARTWORK_URL,
        TrackEntity.DOWNLOADABLE,
        TrackEntity.DOWNLOAD_URL,
        TrackEntity.DURATION,
        TrackEntity.ID,
        TrackEntity.TITLE,
        TrackEntity.URI,
        TrackEntity.USER,
        TrackEntity.TRACK,
        TrackEntity.USERNAME})
@Retention(RetentionPolicy.SOURCE)
public @interface TrackEntity {
    String COLLECTION = "collection";
    String ARTWORK_URL = "artwork_url";
    String DOWNLOADABLE = "downloadable";
    String DOWNLOAD_URL = "download_url";
    String DURATION = "duration";
    String ID = "id";
    String TITLE = "title";
    String URI = "uri";
    String USER = "user";
    String TRACK = "track";
    String USERNAME = "username";
}
