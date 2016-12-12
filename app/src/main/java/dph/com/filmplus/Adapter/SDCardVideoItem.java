package dph.com.filmplus.Adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;

import java.util.HashMap;

import dph.com.filmplus.DPHView.VideoPlayerView;
import dph.com.filmplus.manager.MetaData;
import dph.com.filmplus.manager.VideoPlayerManager;

/**
 * Created by Tran Dac on 12/12/2016.
 */

public class SDCardVideoItem extends BaseVideoItem {

    private final String path;
    private final String mTitle;
    private final String mSize;
    private Bitmap mImageResource;
    protected SDCardVideoItem(String url, String size, String name, VideoPlayerManager<MetaData> videoPlayerManager) {
        super(videoPlayerManager);
        this.path = url;
        this.mSize = size;
        this.mTitle = name;
        //this.mImageResource = img;
    }
    @Override
    public void update(int position, VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        viewHolder.mTitle.setText(mTitle);
        viewHolder.mCover.setVisibility(View.VISIBLE);
        //viewHolder.mCover.setImageBitmap(mImageResource);
        viewHolder.mSize.setText(mSize);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, player, path);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }

}
