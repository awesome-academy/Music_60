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
import phuchh.com.music_60.utils.Constant;

public class TopchartAdapter extends RecyclerView.Adapter<TopchartAdapter.ViewHolder> {
    private List<Track> mTracks;
    private LayoutInflater mInflater;

    public TopchartAdapter(Context context, List<Track> tracks) {
        mInflater = LayoutInflater.from(context);
        mTracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_topchart, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Track track = mTracks.get(i);
        viewHolder.setData(track.getArtworkUrl(), track.getTitle());
    }

    @Override
    public int getItemCount() {
        return mTracks.isEmpty() ? 0 : mTracks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageCover;
        private TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageCover = itemView.findViewById(R.id.image_tc_cover);
            mTextTitle = itemView.findViewById(R.id.text_tc_title);
        }

        public void setData(String imageUrl, String title) {
            if (imageUrl.equals(Constant.NULL_RESULT))
                Glide.with(itemView.getContext()).load(R.drawable.music_cover).into(mImageCover);
            else
                Glide.with(itemView.getContext()).load(imageUrl).into(mImageCover);
            mTextTitle.setText(title);
        }
    }
}
