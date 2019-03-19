package phuchh.com.music_60.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.model.Track;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private int mIndex;

    public NowPlayingAdapter(Context context, List<Track> tracks, int index) {
        mInflater = LayoutInflater.from(context);
        mTracks = tracks;
        mIndex = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_now_playing, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        viewHolder.setData(mTracks.get(i), i);
        if (i == mIndex)
            viewHolder.setPlaying();
    }

    @Override
    public int getItemCount() {
        return mTracks.isEmpty() ? 0 : mTracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextNumber;
        private TextView mTextSong;
        private ImageView mImageNowPlaying;

        public ViewHolder(View view) {
            super(view);
            mTextNumber = view.findViewById(R.id.text_now_number);
            mTextSong = view.findViewById(R.id.text_now_song);
            mImageNowPlaying = view.findViewById(R.id.image_now_playing);
        }

        public void setData(Track track, int number) {
            mTextNumber.setText(String.valueOf(++number));
            mTextSong.setText(track.getTitle());
        }

        public void setPlaying() {
            mImageNowPlaying.setVisibility(View.VISIBLE);
        }
    }
}
