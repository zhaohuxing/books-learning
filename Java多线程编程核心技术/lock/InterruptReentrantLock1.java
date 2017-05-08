import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试: lockInterruptibly()(获取锁) 与 interrupt()(中断)搭配,可以很优雅的产生中断响应
 * 为了达到深入僵局的目的,并没有释放锁,这样就可以看到很明显的效果了。
 * 如果使用lock()(获取锁)，程序陷入永久等待。
 * 还要说明一点：不同对象的锁是不一致的。
 */
public class InterruptReentrantLock1  implements Runnable{
	
	private ReentrantLock lock = new ReentrantLock();

	@Override
	public void run() {
		try {
			
			//lock.lock();
			lock.lockInterruptibly();
			System.out.println(Thread.currentThread().getName() + "获得lock");
			Thread.sleep(500);
			System.out.println(Thread.currentThread().getName() + "执行完毕");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
		//	lock.unlock();
		//	System.out.println(Thread.currentThread().getName() + "释放锁");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		//不同的对象，锁是不一样的
		InterruptReentrantLock1 lock1 = new InterruptReentrantLock1();
		
		Thread t1 = new Thread(lock1);
		Thread t2 = new Thread(lock1);
		t1.setName("t1");
		t2.setName("t2");
		t1.start();
		t2.start();

		t1.interrupt();
	}
}
