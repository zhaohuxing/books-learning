import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
public class TryLockReentrantLock implements Runnable {
	private ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void run() {
		try {
			//其中一个线程获得锁后，占用锁6秒，而另一线程获取锁的时间只有5秒,所以获取不到
			if (lock.tryLock(5, TimeUnit.SECONDS)) {
				System.out.println(Thread.currentThread().getName() + "成功获取锁");
				Thread.sleep(6000);
			} else {
				System.out.println(Thread.currentThread().getName() + "获取锁失败");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		TryLockReentrantLock lock = new TryLockReentrantLock();
		Thread t1 = new Thread(lock);
		Thread t2 = new Thread(lock);
		t1.start();
		t2.start();
	}

}
