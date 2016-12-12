package dph.com.filmplus.Adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import dph.com.filmplus.DPHView.VideoPlayerView;
import dph.com.filmplus.manager.MetaData;
import dph.com.filmplus.manager.VideoPlayerManager;

/**
 * Created by Tran Dac on 11/12/2016.
 */

public class StreamVideoItem extends BaseVideoItem {
    private final String URL;
    private final String mTitle;
    private Bitmap mImageResource;
    protected StreamVideoItem(String url, VideoPlayerManager<MetaData> videoPlayerManager) {
        super(videoPlayerManager);
        this.URL = url;
        this.mTitle = getVideoName(url);
        try {
            this.mImageResource = getThum(url);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    private String getVideoName(String url)
    {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return  fileName;
    }
    @Override
    public void update(int position, VideoViewHolder viewHolder, VideoPlayerManager videoPlayerManager) {
        viewHolder.mTitle.setText(mTitle);
        viewHolder.mCover.setVisibility(View.VISIBLE);
        viewHolder.mCover.setImageBitmap(mImageResource);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player, VideoPlayerManager<MetaData> videoPlayerManager) {
        videoPlayerManager.playNewVideo(currentItemMetaData, player, URL);
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback();
    }
    public static Bitmap getThum(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
