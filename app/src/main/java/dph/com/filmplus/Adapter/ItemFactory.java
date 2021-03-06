package dph.com.filmplus.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import dph.com.filmplus.manager.MetaData;
import dph.com.filmplus.manager.VideoPlayerManager;

public class ItemFactory {

    public static BaseVideoItem createItemFromAsset(String assetName, int imageResource, Activity activity, VideoPlayerManager<MetaData> videoPlayerManager) throws IOException {
        return new AssetVideoItem(assetName, activity.getAssets().openFd(assetName), videoPlayerManager, Picasso.with(activity), imageResource);
    }
    public static BaseVideoItem createItemFromURL(String url, Activity activity, VideoPlayerManager<MetaData> videoPlayerManager)throws IOException {
        return new StreamVideoItem(url, videoPlayerManager);
    }
}
