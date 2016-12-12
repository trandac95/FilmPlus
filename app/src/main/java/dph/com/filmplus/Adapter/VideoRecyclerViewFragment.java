package dph.com.filmplus.Adapter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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


public class VideoRecyclerViewFragment extends Fragment {
    private final ArrayList<BaseVideoItem> mList = new ArrayList<>();

    /**
     * Chỉ 1 video hiển thị rõ (nằm hoàn toàn trên màn hình) được kích hoạt và có thể phát
     * Để tính toán vị trí mà video hiển thị trên màn hình, ta sử dụng {@link SingleListViewItemActiveCalculator}
     */
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    /**
     * ItemsPositionGetter được sử dụng bởi {@link ListItemsVisibilityCalculator} để lấy thông tin
     * vị trí của các item trong RecyclerView và LayoutManager
     */
    private ItemsPositionGetter mItemsPositionGetter;

    /**
     * {@link SingleVideoPlayerManager}Chỉ 1 trình phát video được thiết lập
     */
    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });
    String[] ss;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    public void readData()
    {
        String data;
        InputStream in = getResources().openRawResource(R.raw.link);
        InputStreamReader inreader = new InputStreamReader(in);
        BufferedReader buffreder = new BufferedReader(inreader);
        StringBuilder builder=new StringBuilder();
        if(in!=null)
        {
            try{
                while ((data=buffreder.readLine())!=null)
                {
                    // spilit file video
                    builder.append(data);
                    builder.append("\n");
                }
                in.close();
                ss = builder.toString().split("\n");

            }
            catch(IOException ex){

            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //readData();
        try {
//            for (String s:ss
//                 ) {
//                mList.add(ItemFactory.createItemFromURL(s, getActivity(), mVideoPlayerManager));
//            }
              mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromURL("http://trandac.net/video/video1.mp4", getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
//            mList.add(ItemFactory.createItemFromAsset("video_sample_4.mp4", R.drawable.video_sample_1_pic, getActivity(), mVideoPlayerManager));
            } catch (IOException e) {
            throw new RuntimeException(e);
        }

        View rootView = inflater.inflate(R.layout.fragment_video_recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

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

    @Override
    public void onResume() {
        super.onResume();
        if(!mList.isEmpty()){
            // need to call this method from list view handler in order to have filled list

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // we have to stop any playback in onStop
        mVideoPlayerManager.resetMediaPlayer();
    }

}