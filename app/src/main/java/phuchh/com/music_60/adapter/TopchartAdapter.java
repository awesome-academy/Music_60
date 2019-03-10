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
    private TopchartOnClickListener mListener;

    public TopchartAdapter(Context context, List<Track> tracks) {
        mInflater = LayoutInflater.from(context);
        mTracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_topchart, viewGroup, false);
        return new ViewHolder(view, mListener);
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

    public void setTopchartListener(TopchartOnClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageCover;
        private TextView mTextTitle;
        private TopchartOnClickListener mListener;

        public ViewHolder(View itemView, TopchartOnClickListener listener) {
            super(itemView);
            mImageCover = itemView.findViewById(R.id.image_tc_cover);
            mTextTitle = itemView.findViewById(R.id.text_tc_title);
            mListener = listener;
        }

        public void setData(String imageUrl, String title) {
            if (imageUrl.equals(Constant.NULL_RESULT))
                Glide.with(itemView.getContext()).load(R.drawable.music_cover).into(mImageCover);
            else
                Glide.with(itemView.getContext()).load(imageUrl).into(mImageCover);
            mTextTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            mListener.onTopchartClick();
        }
    }

    public interface TopchartOnClickListener {
        void onTopchartClick();
    }
}
