package dph.com.filmplus.manager;

import dph.com.filmplus.DPHView.VideoPlayerView;
import dph.com.filmplus.PlayerMessageState;

/**
 * to get and set data it needs
 */
public interface VideoPlayerManagerCallback {

    void setCurrentItem(MetaData currentItemMetaData, VideoPlayerView newPlayerView);

    void setVideoPlayerState(VideoPlayerView videoPlayerView, PlayerMessageState playerMessageState);

    PlayerMessageState getCurrentPlayerState();
}
