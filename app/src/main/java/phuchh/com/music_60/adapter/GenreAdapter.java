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

import phuchh.com.music_60.R;
import phuchh.com.music_60.utils.Constant;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    public static final String[] GENRE_TITLES = {"All Music", "All Audio",
            "Alternative Rock", "Ambient", "Classical", "Country"};
    public static final int[] GENRE_COVERS_DRAWABLE = {R.drawable.allmusic,
            R.drawable.allaudio, R.drawable.alternative_rock,
            R.drawable.ambient, R.drawable.classical, R.drawable.country_music};
    private LayoutInflater mLayoutInflater;
    private GenreOnClickListener mGenreOnClickListener;

    public GenreAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.item_genre, viewGroup, false);
        return new ViewHolder(view, mGenreOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(GENRE_COVERS_DRAWABLE[i], GENRE_TITLES[i]);
    }

    @Override
    public int getItemCount() {
        return GENRE_TITLES.length;
    }

    public void setGenreListener(GenreOnClickListener listener) {
        mGenreOnClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageGenre;
        private TextView mTextGenreTitle;
        private GenreOnClickListener mGenreOnClickListener;

        public ViewHolder(@NonNull View itemView, GenreOnClickListener listener) {
            super(itemView);
            mImageGenre = itemView.findViewById(R.id.image_genre_cover);
            mTextGenreTitle = itemView.findViewById(R.id.text_genre_title);
            mGenreOnClickListener = listener;
        }

        public void setData(int image, String title) {
            Glide.with(itemView.getContext()).load(image).into(mImageGenre);
            mTextGenreTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            mGenreOnClickListener.onGenreClick();
        }
    }

    public interface GenreOnClickListener {
        void onGenreClick();
    }
}
