package dph.com.filmplus.DPHView;

import android.util.Pair;

/**
 * Created by Tran Dac on 10/12/2016.
 */

public class ReadyForPlaybackIndicator {
    private Pair<Integer, Integer> mVideoSize;
    private boolean mSurfaceTextureAvailable;
    private boolean mFailedToPrepareUiForPlayback = false;

    public boolean isVideoSizeAvailable() {
        boolean isVideoSizeAvailable = mVideoSize.first != null && mVideoSize.second != null;
        return isVideoSizeAvailable;
    }

    public boolean isSurfaceTextureAvailable() {
        return mSurfaceTextureAvailable;
    }

    public boolean isReadyForPlayback() {
        boolean isReadyForPlayback = isVideoSizeAvailable() && isSurfaceTextureAvailable();
        return isReadyForPlayback;
    }

    public void setSurfaceTextureAvailable(boolean available) {
        mSurfaceTextureAvailable = available;
    }

    public void setVideoSize(Integer videoHeight, Integer videoWidth) {
        mVideoSize = new Pair<>(videoHeight, videoWidth);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + isReadyForPlayback();
    }

    public void setFailedToPrepareUiForPlayback(boolean failed) {
        mFailedToPrepareUiForPlayback = failed;
    }

    public boolean isFailedToPrepareUiForPlayback() {
        return mFailedToPrepareUiForPlayback;
    }
}
