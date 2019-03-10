package phuchh.com.music_60.utils;

import java.util.Locale;

import phuchh.com.music_60.BuildConfig;

public class StringUtil {
    public static String getUrlTrackByGenre(String genre, int limit, int offset) {
        return String.format(Locale.ENGLISH,"%s%s%s&%s=%s&%s=%d&%s=%d",
                Constant.BASE_URL, Constant.PARAM_MUSIC_GENRE, genre, Constant.CLIENT_ID,
                BuildConfig.API_KEY, Constant.LIMIT, limit, Constant.PARAM_OFFSET, offset);
    }

    public static String getUrlStreamTrack(String uriTrack) {
        return String.format("%s/%s?%s=%s", uriTrack, Constant.PARAM_STREAM,
                Constant.CLIENT_ID, BuildConfig.API_KEY);
    }
}
