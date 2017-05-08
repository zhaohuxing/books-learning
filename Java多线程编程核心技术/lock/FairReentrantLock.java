import java.util.concurrent.locks.ReentrantLock;

public class FairReentrantLock implements Runnable {
	private ReentrantLock lock = new ReentrantLock(true);

	@Override
	public void run() {
		while (true) {
			lock.lock();
			System.out.println(Thread.currentThread().getName() + "获得锁");
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		FairReentrantLock lock = new FairReentrantLock();
		Thread t1 = new Thread(lock);
		Thread t2 = new Thread(lock);
		t1.setName("t1");
		t2.setName("t2");
		t1.start();
		t2.start();
	}
}
