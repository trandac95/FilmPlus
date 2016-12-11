package dph.com.filmplus;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerQueueLock {

    private static final String TAG = PlayerQueueLock.class.getSimpleName();
    private static final boolean SHOW_LOGS = false;
    private final ReentrantLock mQueueLock = new ReentrantLock();
    private final Condition mProcessQueueCondition = mQueueLock.newCondition();

    public void lock(String owner){
        mQueueLock.lock();
    }

    public void unlock(String owner){
        mQueueLock.unlock();
    }

    public boolean isLocked(String owner){
        boolean isLocked = mQueueLock.isLocked();
        return isLocked;
    }

    public void wait(String owner) throws InterruptedException {
        mProcessQueueCondition.await();
    }

    public void notify(String owner) {
        mProcessQueueCondition.signal();
    }
}
