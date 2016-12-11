package dph.com.filmplus.DPHView;

/**
 * Created by Tran Dac on 10/12/2016.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is player implementation based on {@link TextureView}
 * It encapsulates {@link MediaPlayer}.
 *
 * It ensures that MediaPlayer methods are called from not main thread.
 * MediaPlayer methods are directly connected with hardware. That's why they should not be called from UI thread
 *
 */
public class VideoPlayerView extends ScalableVideoView
        implements TextureView.SurfaceTextureListener,
        MediaPlayerWrapper.MainThreadMediaPlayerListener,
        MediaPlayerWrapper.VideoStateListener {
    private String TAG;

    private static final String IS_VIDEO_MUTED = "IS_VIDEO_MUTED";

    /**
     * MediaPlayerWrapper instance.
     * If you need to use it you should synchronize in on {@link VideoPlayerView#mReadyForPlaybackIndicator} in order to have a consistent state.
     * Also you should call it from background thread to avoid ANR
     */
    private MediaPlayerWrapper mMediaPlayer;
    private HandlerThreadExtension mViewHandlerBackgroundThread;

    /**
     * A Listener that propagates {@link MediaPlayer} listeners is background thread.
     * Probably call of this listener should also need to be synchronized with it creation and destroy places.
     */
    private BackgroundThreadMediaPlayerListener mMediaPlayerListenerBackgroundThread;

    private MediaPlayerWrapper.VideoStateListener mVideoStateListener;
    private TextureView.SurfaceTextureListener mLocalSurfaceTextureListener;

    private AssetFileDescriptor mAssetFileDescriptor;
    private String mPath;

    private final ReadyForPlaybackIndicator mReadyForPlaybackIndicator = new ReadyForPlaybackIndicator();

    private final Set<MediaPlayerWrapper.MainThreadMediaPlayerListener> mMediaPlayerMainThreadListeners = new HashSet<>();

    public MediaPlayerWrapper.State getCurrentState() {
        synchronized (mReadyForPlaybackIndicator) {
            return mMediaPlayer.getCurrentState();
        }
    }

    public AssetFileDescriptor getAssetFileDescriptorDataSource() {
        return mAssetFileDescriptor;
    }

    public String getVideoUrlDataSource() {
        return mPath;
    }

    public interface BackgroundThreadMediaPlayerListener {
        void onVideoSizeChangedBackgroundThread(int width, int height);

        void onVideoPreparedBackgroundThread();

        void onVideoCompletionBackgroundThread();

        void onErrorBackgroundThread(int what, int extra);
    }

    public VideoPlayerView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        //super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void checkThread() {
        if(Looper.myLooper() == Looper.getMainLooper()){
            throw new RuntimeException("cannot be in main thread");
        }
    }

    public void reset() {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            mMediaPlayer.reset();
        }
    }

    public void release() {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            mMediaPlayer.release();
        }
    }

    public void clearPlayerInstance() {

        checkThread();

        synchronized (mReadyForPlaybackIndicator){
            mReadyForPlaybackIndicator.setVideoSize(null, null);
            mMediaPlayer.clearAll();
            mMediaPlayer = null;
        }
    }

    public void createNewPlayerInstance() {
        checkThread();
        synchronized (mReadyForPlaybackIndicator){

            mMediaPlayer = new MediaPlayerWrapperImpl();

            mReadyForPlaybackIndicator.setVideoSize(null, null);
            mReadyForPlaybackIndicator.setFailedToPrepareUiForPlayback(false);

            if(mReadyForPlaybackIndicator.isSurfaceTextureAvailable()){
                SurfaceTexture texture = getSurfaceTexture();
                mMediaPlayer.setSurfaceTexture(texture);
            } else {
            }
            mMediaPlayer.setMainThreadMediaPlayerListener(this);
            mMediaPlayer.setVideoStateListener(this);
        }
    }

    public void prepare() {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            mMediaPlayer.prepare();
        }
    }

    public void stop() {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            mMediaPlayer.stop();
        }
    }

    private void notifyOnVideoStopped() {
        List<MediaPlayerWrapper.MainThreadMediaPlayerListener> listCopy;
        synchronized (mMediaPlayerMainThreadListeners){
            listCopy = new ArrayList<>(mMediaPlayerMainThreadListeners);
        }
        for (MediaPlayerWrapper.MainThreadMediaPlayerListener listener : listCopy){
            listener.onVideoStoppedMainThread();
        }
    }

    private boolean isVideoSizeAvailable() {
        boolean isVideoSizeAvailable = getContentHeight() != null && getContentWidth() != null;
        return isVideoSizeAvailable;
    }

    public void start(){
        synchronized (mReadyForPlaybackIndicator){
            if(mReadyForPlaybackIndicator.isReadyForPlayback()){
                mMediaPlayer.start();
            } else {
                if(!mReadyForPlaybackIndicator.isFailedToPrepareUiForPlayback()){
                    try {
                        mReadyForPlaybackIndicator.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if(mReadyForPlaybackIndicator.isReadyForPlayback()){
                        mMediaPlayer.start();
                    } else {
                         }
                } else {
                }
            }
        }
    }

    private void initView() {
        if(!isInEditMode()){
            TAG = "" + this;
            setScaleType(ScalableVideoView.ScaleType.CENTER_CROP);
            super.setSurfaceTextureListener(this);
        }
    }

    @Override
    public final void setSurfaceTextureListener(TextureView.SurfaceTextureListener listener){
        mLocalSurfaceTextureListener = listener;
    }

    public void setDataSource(String path) {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            try {
                mMediaPlayer.setDataSource(path);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mPath = path;
        }
    }

    public void setDataSource(AssetFileDescriptor assetFileDescriptor) {
        checkThread();
        synchronized (mReadyForPlaybackIndicator) {
            try {
                mMediaPlayer.setDataSource(assetFileDescriptor);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            mAssetFileDescriptor = assetFileDescriptor;
        }
    }

    public void setOnVideoStateChangedListener(MediaPlayerWrapper.VideoStateListener listener) {
        mVideoStateListener = listener;
        checkThread();
        synchronized (mReadyForPlaybackIndicator){
            mMediaPlayer.setVideoStateListener(listener);
        }
    }

    public void addMediaPlayerListener(MediaPlayerWrapper.MainThreadMediaPlayerListener listener) {
        synchronized (mMediaPlayerMainThreadListeners){
            mMediaPlayerMainThreadListeners.add(listener);
        }
    }

    public void setBackgroundThreadMediaPlayerListener(BackgroundThreadMediaPlayerListener listener) {
        mMediaPlayerListenerBackgroundThread = listener;
    }

    @Override
    public void onVideoSizeChangedMainThread(int width, int height) {

        if (width  != 0 && height != 0) {
            setContentWidth(width);
            setContentHeight(height);

            onVideoSizeAvailable();
        } else {

            synchronized (mReadyForPlaybackIndicator){
                mReadyForPlaybackIndicator.setFailedToPrepareUiForPlayback(true);
                mReadyForPlaybackIndicator.notifyAll();
            }
        }

        notifyOnVideoSizeChangedMainThread(width, height);
    }

    private void notifyOnVideoSizeChangedMainThread(int width, int height) {
        List<MediaPlayerWrapper.MainThreadMediaPlayerListener> listCopy;
        synchronized (mMediaPlayerMainThreadListeners){
            listCopy = new ArrayList<>(mMediaPlayerMainThreadListeners);
        }
        for (MediaPlayerWrapper.MainThreadMediaPlayerListener listener : listCopy){
            listener.onVideoSizeChangedMainThread(width, height);
        }
    }

    private final Runnable mVideoCompletionBackgroundThreadRunnable = new Runnable() {
        @Override
        public void run() {
            mMediaPlayerListenerBackgroundThread.onVideoSizeChangedBackgroundThread(getContentHeight(), getContentWidth());
        }
    };

    @Override
    public void onVideoCompletionMainThread() {
        notifyOnVideoCompletionMainThread();
        if (mMediaPlayerListenerBackgroundThread != null) {
            mViewHandlerBackgroundThread.post(mVideoCompletionBackgroundThreadRunnable);
        }
    }

    private void notifyOnVideoCompletionMainThread() {
        List<MediaPlayerWrapper.MainThreadMediaPlayerListener> listCopy;
        synchronized (mMediaPlayerMainThreadListeners){
            listCopy = new ArrayList<>(mMediaPlayerMainThreadListeners);
        }
        for (MediaPlayerWrapper.MainThreadMediaPlayerListener listener : listCopy) {
            listener.onVideoCompletionMainThread();
        }
    }

    private void notifyOnVideoPreparedMainThread() {
        List<MediaPlayerWrapper.MainThreadMediaPlayerListener> listCopy;
        synchronized (mMediaPlayerMainThreadListeners){
            listCopy = new ArrayList<>(mMediaPlayerMainThreadListeners);
        }
        for (MediaPlayerWrapper.MainThreadMediaPlayerListener listener : listCopy) {
            listener.onVideoPreparedMainThread();
        }
    }

    private void notifyOnErrorMainThread(int what, int extra) {
        List<MediaPlayerWrapper.MainThreadMediaPlayerListener> listCopy;
        synchronized (mMediaPlayerMainThreadListeners){
            listCopy = new ArrayList<>(mMediaPlayerMainThreadListeners);
        }
        for (MediaPlayerWrapper.MainThreadMediaPlayerListener listener : listCopy) {
            listener.onErrorMainThread(what, extra);
        }
    }

    private final Runnable mVideoPreparedBackgroundThreadRunnable = new Runnable() {
        @Override
        public void run() {
            mMediaPlayerListenerBackgroundThread.onVideoPreparedBackgroundThread();
        }
    };

    @Override
    public void onVideoPreparedMainThread() {
        notifyOnVideoPreparedMainThread();

        if (mMediaPlayerListenerBackgroundThread != null) {
            mViewHandlerBackgroundThread.post(mVideoPreparedBackgroundThreadRunnable);
        }
    }

    @Override
    public void onErrorMainThread(final int what, final int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                printErrorExtra(extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                printErrorExtra(extra);
                break;
        }

        notifyOnErrorMainThread(what, extra);

        if (mMediaPlayerListenerBackgroundThread != null) {
            mViewHandlerBackgroundThread.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerListenerBackgroundThread.onErrorBackgroundThread(what, extra);
                }
            });
        }
    }

    @Override
    public void onBufferingUpdateMainThread(int percent) {

    }

    @Override
    public void onVideoStoppedMainThread() {
        notifyOnVideoStopped();
    }

    private void printErrorExtra(int extra) {
        switch (extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                break;
        }
    }

    private final Runnable mVideoSizeAvailableRunnable = new Runnable() {
        @Override
        public void run() {

            synchronized (mReadyForPlaybackIndicator) {

                mReadyForPlaybackIndicator.setVideoSize(getContentHeight(), getContentWidth());

                if (mReadyForPlaybackIndicator.isReadyForPlayback()) {

                    mReadyForPlaybackIndicator.notifyAll();
                }
            }
            if (mMediaPlayerListenerBackgroundThread != null) {
                mMediaPlayerListenerBackgroundThread.onVideoSizeChangedBackgroundThread(getContentHeight(), getContentWidth());
            }
        }
    };

    private void onVideoSizeAvailable() {

        updateTextureViewSize();

        if(isAttachedToWindow()){
            mViewHandlerBackgroundThread.post(mVideoSizeAvailableRunnable);
        }
    }


    public void muteVideo() {
        synchronized (mReadyForPlaybackIndicator) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(IS_VIDEO_MUTED, true).commit();
            mMediaPlayer.setVolume(0, 0);
        }
    }

    public void unMuteVideo() {
        synchronized (mReadyForPlaybackIndicator) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(IS_VIDEO_MUTED, false).commit();
            mMediaPlayer.setVolume(1, 1);
        }
    }

    public boolean isAllVideoMute() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(IS_VIDEO_MUTED, false);
    }

    public void pause() {
        synchronized (mReadyForPlaybackIndicator) {
            mMediaPlayer.pause();
        }
    }

    /**
     * @see MediaPlayer#getDuration()
     */
    public int getDuration() {
        synchronized (mReadyForPlaybackIndicator) {
            return mMediaPlayer.getDuration();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if(mLocalSurfaceTextureListener != null){
            mLocalSurfaceTextureListener.onSurfaceTextureAvailable(surfaceTexture, width, height);
        }
        notifyTextureAvailable();
    }

    private void notifyTextureAvailable() {

        mViewHandlerBackgroundThread.post(new Runnable() {
            @Override
            public void run() {

                synchronized (mReadyForPlaybackIndicator) {

                    if (mMediaPlayer != null) {
                        mMediaPlayer.setSurfaceTexture(getSurfaceTexture());
                    } else {
                        mReadyForPlaybackIndicator.setVideoSize(null, null);
                    }
                    mReadyForPlaybackIndicator.setSurfaceTextureAvailable(true);

                    if (mReadyForPlaybackIndicator.isReadyForPlayback()) {
                        mReadyForPlaybackIndicator.notifyAll();
                    }
                }
            }
        });
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if(mLocalSurfaceTextureListener != null){
            mLocalSurfaceTextureListener.onSurfaceTextureSizeChanged(surface, width, height);
        }
    }

    /**
     * Note : this method might be called after {@link #onDetachedFromWindow()}
     * @param surface
     * @return
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if(mLocalSurfaceTextureListener != null){
            mLocalSurfaceTextureListener.onSurfaceTextureDestroyed(surface);
        }

        if(isAttachedToWindow()){
            mViewHandlerBackgroundThread.post(new Runnable() {
                @Override
                public void run() {

                    synchronized (mReadyForPlaybackIndicator) {
                        mReadyForPlaybackIndicator.setSurfaceTextureAvailable(false);

                        /** we have to notify a Thread may be in wait() state in {@link VideoPlayerView#start()} method*/
                        mReadyForPlaybackIndicator.notifyAll();
                    }
                }
            });
        }

        // We have to release this surface manually for better control.
        // Also we do this because we return false from this method
        surface.release();
        return false;
    }

    @Override
    public boolean isAttachedToWindow() {
        return mViewHandlerBackgroundThread != null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        if (SHOW_LOGS) Logger.v(TAG, "onSurfaceTextureUpdated, mIsVideoStartedCalled " + mIsVideoStartedCalled.get() + ", mMediaPlayer.getState() " + mMediaPlayer.getState());
        if(mLocalSurfaceTextureListener != null){
            mLocalSurfaceTextureListener.onSurfaceTextureUpdated(surface);
        }
    }

    public interface PlaybackStartedListener {
        void onPlaybackStarted();
    }

    @Override
    public void onVideoPlayTimeChanged(int positionInMilliseconds) {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        boolean isInEditMode = isInEditMode();
        if(!isInEditMode){
            mViewHandlerBackgroundThread = new HandlerThreadExtension(TAG, false);
            mViewHandlerBackgroundThread.startThread();
        }
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        boolean isInEditMode = isInEditMode();
        if(!isInEditMode){
            mViewHandlerBackgroundThread.postQuit();
            mViewHandlerBackgroundThread = null;
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        boolean isInEditMode = isInEditMode();
        if (!isInEditMode) {

            switch (visibility){
                case VISIBLE:
                    break;
                case INVISIBLE:
                case GONE:
                    synchronized (mReadyForPlaybackIndicator){
                        // have to notify worker thread in case we exited this screen without getting ready for playback
                        mReadyForPlaybackIndicator.notifyAll();
                    }
            }
        }
    }

    private static String visibilityStr(int visibility) {
        switch (visibility){
            case VISIBLE:
                return "VISIBLE";
            case INVISIBLE:
                return "INVISIBLE";
            case GONE:
                return "GONE";
            default:
                throw new RuntimeException("unexpected");
        }
    }
}
