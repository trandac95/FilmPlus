package dph.com.filmplus.calculator;

import android.view.View;

import dph.com.filmplus.items.ListItem;

public class DefaultSingleItemCalculatorCallback implements SingleListViewItemActiveCalculator.Callback<ListItem>{

    @Override
    public void activateNewCurrentItem(ListItem newListItem, View newView, int newViewPosition) {

        /**
         * Here you can do whatever you need with a newly "active" ListItem.
         */
        newListItem.setActive(newView, newViewPosition);
    }

    @Override
    public void deactivateCurrentItem(ListItem listItemToDeactivate, View view, int position) {
        /**
         * When view need to stop being active we call deactivate.
         */
        listItemToDeactivate.deactivate(view, position);
    }
}
