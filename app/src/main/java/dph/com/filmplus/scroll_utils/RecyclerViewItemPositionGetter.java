package dph.com.filmplus.scroll_utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import dph.com.filmplus.calculator.ListItemsVisibilityCalculator;


/**
 * This class is an API for {@link ListItemsVisibilityCalculator}
 * Using this class is can access all the data from RecyclerView
 * Created by danylo.volokh on 06.01.2016.
 */
public class RecyclerViewItemPositionGetter implements ItemsPositionGetter {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public RecyclerViewItemPositionGetter(LinearLayoutManager layoutManager, RecyclerView recyclerView) {
        mLayoutManager = layoutManager;
        mRecyclerView = recyclerView;
    }

    @Override
    public View getChildAt(int position) {
        View view = mLayoutManager.getChildAt(position);
        return view;
    }

    @Override
    public int indexOfChild(View view) {
        int indexOfChild = mRecyclerView.indexOfChild(view);
        return indexOfChild;
    }

    @Override
    public int getChildCount() {
        int childCount = mRecyclerView.getChildCount();
        return childCount;
    }

    @Override
    public int getLastVisiblePosition() {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    @Override
    public int getFirstVisiblePosition() {
        return mLayoutManager.findFirstVisibleItemPosition();
    }
}
