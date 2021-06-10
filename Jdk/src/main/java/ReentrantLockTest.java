import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
}

/**
 * 公平锁
 */
class Fair {
    public static void main(String[] args) {
        // 默认未非公平锁
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.getHoldCount();
        //尝试上锁
        reentrantLock.tryLock();
        reentrantLock.lock();
        reentrantLock.getQueueLength();
    }
}