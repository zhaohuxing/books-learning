import java.util.concurrent.locks.ReentrantLock;

public class InterruptReentrantLock  implements Runnable{
	
	//目的：使线程获取锁，产生死锁。然后使用interrupt来中断其中一线程.
	private ReentrantLock lock1 = new ReentrantLock();
	private ReentrantLock lock2 = new ReentrantLock();
	int lock;

	public InterruptReentrantLock(int lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		try {
			//让lock == 1的线程获取lock1, 让lock == 2 的线程获取lock2,
			//在两者为释放锁的情况下，二者相互申请对方的锁，陷入死锁。
			if (lock == 1) {
			//	lock1.lockInterruptibly();
				lock1.lock();
				System.out.println("线程1已获取lock1");
				//确保线程2获取lock2，这里休眠3秒
				Thread.sleep(3000);
				
				//lock2.lockInterruptibly();
				lock2.lock();
				System.out.println("线程1已获取lock2");
			} else {
				
				//lock2.lockInterruptibly();
				lock2.lock();
				System.out.println("线程2已获取lock2");
				//确保线程1获取lock1.这里休眠3秒
				Thread.sleep(3000);

			//	lock1.lockInterruptibly();
				lock1.lock();
				System.out.println("线程2已获取lock1");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
/*			if (lock1.isHeldByCurrentThread()) {
				lock1.unlock();
			}
			if (lock2.isHeldByCurrentThread()) {
				lock2.unlock();
			}
		*/
		}
	}

	public static void main(String[] args) throws InterruptedException {
		InterruptReentrantLock lock1 = new InterruptReentrantLock(1);
		InterruptReentrantLock lock2 = new InterruptReentrantLock(2);
		
		Thread t1 = new Thread(lock1);
		Thread t2 = new Thread(lock2);

		t1.start();
		t2.start();

	}
}
