package phuchh.com.music_60.data.source.remote;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import phuchh.com.music_60.data.model.Track;
import phuchh.com.music_60.data.source.TrackDataSource;
import phuchh.com.music_60.utils.Constant;
import phuchh.com.music_60.utils.StringUtil;
import phuchh.com.music_60.utils.TrackEntity;

public class FetchTrack extends AsyncTask<String, Void, List<Track>> {
    private TrackDataSource.FetchTrackCallback mCallback;

    public FetchTrack(TrackDataSource.FetchTrackCallback callback) {
        mCallback = callback;
    }

    public static String getJSONStringFromURL(String urlString) throws IOException {
        HttpURLConnection httpURLConnection;
        URL url = new URL(urlString);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(Constant.REQUEST_METHOD_GET);
        httpURLConnection.setReadTimeout(Constant.READ_TIME_OUT);
        httpURLConnection.setConnectTimeout(Constant.CONNECT_TIME_OUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(Constant.BREAK_LINE);
        }
        br.close();
        httpURLConnection.disconnect();
        return sb.toString();
    }

    @Override
    protected List<Track> doInBackground(String... strings) {
        try {
            JSONObject jsonObject = new JSONObject(getJSONStringFromURL(strings[0]));
            return getTracksFromJsonObject(jsonObject);
        } catch (IOException e) {
            mCallback.onFail(e.getMessage());
        } catch (JSONException e) {
            mCallback.onFail(e.getMessage());
        }
        return null;
    }

    private List<Track> getTracksFromJsonObject(JSONObject jsonObject) {
        List<Track> tracks = new ArrayList<>();
        JSONArray jsonCollection = jsonObject.optJSONArray(TrackEntity.COLLECTION);
        int lenght = jsonCollection.length();
        for (int i = 0; i < lenght; i++) {

            JSONObject jsonObjectTrack = jsonCollection.optJSONObject(i);
            JSONObject trackObj = jsonObjectTrack.optJSONObject(TrackEntity.TRACK);
            Track track = parseJsonObjectToTrackObject(trackObj);
            if (track != null) {
                tracks.add(track);
            }

        }
        return tracks;
    }

    @Override
    protected void onPostExecute(List<Track> tracks) {
        if (tracks == null)
            mCallback.onFail(Constant.INTERNET_NOT_AVAIABLE);
        if (mCallback != null)
            mCallback.onSuccess(tracks);
    }

    private Track parseJsonObjectToTrackObject(JSONObject jsonTrack) {
        Track track = new Track();
        try {
            JSONObject jsonUser = jsonTrack.getJSONObject(TrackEntity.USER);
            track.setArtworkUrl(jsonTrack.optString(TrackEntity.ARTWORK_URL));
            track.setDownloadable(jsonTrack.optBoolean(TrackEntity.DOWNLOADABLE));
            track.setDownloadUrl(jsonTrack.optString(TrackEntity.DOWNLOAD_URL));
            track.setDuration(jsonTrack.optInt(TrackEntity.DURATION));
            track.setId(jsonTrack.optInt(TrackEntity.ID));
            track.setTitle(jsonTrack.optString(TrackEntity.TITLE));
            track.setUri(StringUtil.getUrlStreamTrack(jsonTrack.optString(TrackEntity.URI)));
            track.setUserName(jsonUser.optString(TrackEntity.USERNAME));
        } catch (JSONException e) {
            return null;
        }
        return track;
    }
}
