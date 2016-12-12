package dph.com.filmplus.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import dph.com.filmplus.manager.VideoPlayerManager;

/**
 * Created by Tran Dac on 12/12/2016.
 */

public class SDCardVideoRecyclerAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private final VideoPlayerManager mVideoPlayerManager;
    private final List<BaseVideoItem> mList;
    private final Context mContext;
    public SDCardVideoRecyclerAdapter(VideoPlayerManager mVideoPlayerManager, Context mContext, List<BaseVideoItem> mList) {
        this.mVideoPlayerManager = mVideoPlayerManager;
        this.mList = mList;
        this.mContext = mContext;
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
        BaseVideoItem videoItem = mList.get(pos);
        View resultView = videoItem.createView(parent, mContext.getResources().getDisplayMetrics().widthPixels);
        return new VideoViewHolder(resultView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        BaseVideoItem videoItem = mList.get(position);
        videoItem.update(position, holder, mVideoPlayerManager);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
