import java.util.concurrent.locks.ReentrantReadWriteLock;
public class ReadReentrantReadWriteLock {
	
	public static void main(String[] args) {
		Service service = new Service();
		ThreadA a = new ThreadA(service);
		a.setName("A");
		ThreadB b = new ThreadB(service);
		b.setName("B");
		a.start();
		b.start();
	}
}

class Service {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public void read() {
		
		try {
			lock.readLock().lock();//读锁
			System.out.println("获得读锁:" + Thread.currentThread().getName() +
					" " + System.currentTimeMillis());
		} finally {
		//	lock.readLock().unlock();
		//	 attempt to unlock read lock, not locked by current thread
		} 

	}
}

class ThreadA extends Thread {
	private Service service;

	public ThreadA(Service service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		try {
			service.read();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ThreadB extends Thread {
	private Service service;

	public ThreadB(Service service) {
		super();
		this.service = service;
	}

	@Override
	public void run() {
		try {
			service.read();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
