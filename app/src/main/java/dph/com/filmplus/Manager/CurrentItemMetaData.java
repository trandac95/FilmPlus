package dph.com.filmplus.manager;
import android.view.View;

import dph.com.filmplus.manager.MetaData;

public class CurrentItemMetaData implements MetaData {

    public final int positionOfCurrentItem;
    public final View currentItemView;

    public CurrentItemMetaData(int positionOfCurrentItem, View currentItemView) {
        this.positionOfCurrentItem = positionOfCurrentItem;
        this.currentItemView = currentItemView;
    }

    @Override
    public String toString() {
        return "CurrentItemMetaData{" +
                "positionOfCurrentItem=" + positionOfCurrentItem +
                ", currentItemView=" + currentItemView +
                '}';
    }
}
