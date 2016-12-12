package dph.com.filmplus.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import dph.com.filmplus.R;
import dph.com.filmplus.calculator.DefaultSingleItemCalculatorCallback;
import dph.com.filmplus.calculator.ListItemsVisibilityCalculator;
import dph.com.filmplus.calculator.SingleListViewItemActiveCalculator;
import dph.com.filmplus.manager.MetaData;
import dph.com.filmplus.manager.PlayerItemChangeListener;
import dph.com.filmplus.manager.SingleVideoPlayerManager;
import dph.com.filmplus.manager.VideoPlayerManager;
import dph.com.filmplus.scroll_utils.ItemsPositionGetter;
import dph.com.filmplus.scroll_utils.RecyclerViewItemPositionGetter;

/**
 * Created by Tran Dac on 12/12/2016.
 */

public class SDCardVideoRecyclerFragment extends Fragment {

    private final ArrayList<BaseVideoItem> mList = new ArrayList<>();
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ItemsPositionGetter mItemsPositionGetter;
    //private Context context;
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getData();
        VideoRecyclerViewAdapter videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(mVideoPlayerManager, getActivity(), mList);

        mRecyclerView.setAdapter(videoRecyclerViewAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;
                if(scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()){

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(!mList.isEmpty()){
                    mVideoVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });
        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);

        return rootView;
    }

    private void addListVideo(Cursor c){
        long id = c.getLong(0);
        String filename= c.getString(1);
        String display_name= c.getString(2);
        String size= c.getString(3);
        mList.add(new SDCardVideoItem(filename, size, display_name, mVideoPlayerManager ));
    }
    private void getData(){
        String[] proj = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,//filename
                MediaStore.Video.Media.DISPLAY_NAME,//video name
                MediaStore.Video.Media.DURATION };
        //Cursor cIn = getActivity().getContentResolver().query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, proj,null,null,null);
        //managedQuery() is a method to query any content(External, Internal)
        //MediaStore.Video.Media.INTERNAL_CONTENT_URI: The URI of the content provider to query.
        //proj: List of columns to return.
        Cursor cEx = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj,null,null,null);
//        if (cIn != null) {
//            while (cIn.moveToNext()) {//browse the cursor
//                addListVideo(cIn);
//            }
//        }

        if (cEx != null) {
            while (cEx.moveToNext()) {//browse the cursor
                addListVideo(cEx);
            }
        }
    }
    Bitmap getBitmap(long id)
    {
        ContentResolver crThumb = getActivity().getContentResolver();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
                crThumb, id, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND,
                options);
        return curThumb;
    }
}
