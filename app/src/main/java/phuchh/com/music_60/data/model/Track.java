package phuchh.com.music_60.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    private String mArtworkUrl;
    private boolean mIsDownloadable;
    private String mDownloadUrl;
    private int mDuration;
    private int mId;
    private String mTitle;
    private String mUri;
    private String mUserName;

    public Track() {
        mArtworkUrl = "";
        mIsDownloadable = false;
        mDownloadUrl = "";
        mDuration = 0;
        mId = 0;
        mTitle = "";
        mUri = "";
        mUserName = "";
    }

    protected Track(Parcel in) {
        mArtworkUrl = in.readString();
        mIsDownloadable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        mDuration = in.readInt();
        mId = in.readInt();
        mTitle = in.readString();
        mUri = in.readString();
        mUserName = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mArtworkUrl);
        parcel.writeByte((byte) (mIsDownloadable ? 1 : 0));
        parcel.writeString(mDownloadUrl);
        parcel.writeInt(mDuration);
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mUri);
        parcel.writeString(mUserName);
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isDownloadable() {
        return mIsDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        mIsDownloadable = downloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}
