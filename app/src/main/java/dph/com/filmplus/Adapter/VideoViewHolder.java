package dph.com.filmplus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dph.com.filmplus.DPHView.VideoPlayerView;
import dph.com.filmplus.R;

/**
 * Created by Tran Dac on 07/12/2016.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public final VideoPlayerView mPlayer;
    public final TextView mTitle;
    public final ImageView mCover;
    public final TextView mSize;

    public VideoViewHolder(View view) {
        super(view);
        mPlayer = (VideoPlayerView) view.findViewById(R.id.player);
        mTitle = (TextView) view.findViewById(R.id.title);
        mCover = (ImageView) view.findViewById(R.id.cover);
        mSize = (TextView) view.findViewById(R.id.size);
    }
}
