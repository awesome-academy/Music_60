package phuchh.com.music_60.utils;

import phuchh.com.music_60.R;

public class Constant {
    public static final String BASE_URL = "https://api-v2.soundcloud.com/";
    public static final String PARAM_MUSIC_GENRE = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_STREAM = "stream";
    public static final String CLIENT_ID = "client_id";
    public static final String REQUEST_METHOD_GET = "GET";
    public static final int READ_TIME_OUT = 5000;
    public static final int CONNECT_TIME_OUT = 5000;
    public static final String LIMIT = "limit";
    public static final String ALL_MUSIC = "all-music";
    public static final int LIMIT_DEFAULT = 30;
    public static final int OFFSET_DEFAULT = 0;
    public static final String NULL_RESULT = "null";
    public static final String[] MUSIC_GENRES = {"all-music", "all-audio",
            "alternativerock", "ambient", "classical", "country"};
    public static final String BREAK_LINE = "\n";
    public static final String INTERNET_NOT_AVAIABLE = "The internet is not available";
    public static final int UPDATE_SEEKBAR = 283;
}
