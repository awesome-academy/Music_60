package phuchh.com.music_60.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import phuchh.com.music_60.R;
import phuchh.com.music_60.data.model.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private Context mContext;
    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private ListTrackOnClickListener mTrackListener;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_list_track, viewGroup, false);
        return new ViewHolder(view, mTrackListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(mTracks.get(i));
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public void setListTrackListener(ListTrackOnClickListener trackListener) {
        mTrackListener = trackListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageTrack;
        private TextView mTextNameTrack;
        private ListTrackOnClickListener mTrackListener;

        public ViewHolder(@NonNull View itemView, ListTrackOnClickListener trackListener) {
            super(itemView);
            mImageTrack = itemView.findViewById(R.id.image_track);
            mTextNameTrack = itemView.findViewById(R.id.text_name_track);
            mTrackListener = trackListener;
            itemView.setOnClickListener(this);
        }

        public void setData(Track track) {
            mTextNameTrack.setText(track.getTitle());
            Glide.with(itemView.getContext())
                    .load(track.getArtworkUrl())
                    .placeholder(R.drawable.music_cover)
                    .into(mImageTrack);
        }

        @Override
        public void onClick(View view) {
            mTrackListener.onListTrackClick(getAdapterPosition());
        }
    }

    public interface ListTrackOnClickListener {
        void onListTrackClick(int position);
    }
}
