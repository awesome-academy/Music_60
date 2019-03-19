package phuchh.com.music_60.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.model.Track;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private int mIndex;
    private NowPlayingOnClickListener mOnClickListener;
    private NowPlayingOnChangeListener mOnChangeListener;

    public NowPlayingAdapter(Context context, List<Track> tracks, int index) {
        mInflater = LayoutInflater.from(context);
        mTracks = tracks;
        mIndex = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_now_playing, viewGroup, false);
        return new ViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        viewHolder.setData(mTracks.get(i));
        if (i == mIndex)
            viewHolder.setPlaying();
    }

    @Override
    public int getItemCount() {
        return mTracks.isEmpty() ? 0 : mTracks.size();
    }

    @Override
    public void onItemDismiss(int position) {
        mTracks.remove(position);
        mOnChangeListener.onNowPlayingRemove(mTracks, position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTracks, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTracks, i, i - 1);
            }
        }
        mOnChangeListener.onNowPlayingMove(mTracks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setOnClickListener(NowPlayingOnClickListener listener) {
        mOnClickListener = listener;
    }

    public void setOnChangeListener(NowPlayingOnChangeListener listener){
        mOnChangeListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextSong;
        private ImageView mImageNowPlaying;
        private NowPlayingOnClickListener mListener;

        public ViewHolder(View view, NowPlayingOnClickListener listener) {
            super(view);
            mTextSong = view.findViewById(R.id.text_now_song);
            mImageNowPlaying = view.findViewById(R.id.image_now_playing);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        public void setData(Track track) {
            mTextSong.setText(track.getTitle());
        }

        public void setPlaying() {
            mImageNowPlaying.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            mListener.onNowPlayingClick(getAdapterPosition());
        }
    }

    public interface NowPlayingOnClickListener {
        void onNowPlayingClick(int position);
    }

    public interface NowPlayingOnChangeListener {
        void onNowPlayingMove(List<Track> tracks, int from, int to);
        void onNowPlayingRemove(List<Track> tracks, int position);
    }}
