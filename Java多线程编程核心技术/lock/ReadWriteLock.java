import java.util.concurrent.locks.ReentrantReadWriteLock;
public class ReadWriteLock {
	
	public static void main(String[] args) {
		Service service = new Service();
		ThreadA a = new ThreadA(service);
		ThreadB b = new ThreadB(service);
		a.setName("a");
		b.setName("b");
		a.start();
		b.start();
	
	}
}

class Service {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public void read() {
		try {
			lock.readLock().lock();
			System.out.println("获取读锁:" + Thread.currentThread().getName() 
					+ " " + System.currentTimeMillis());		
		} finally {
			lock.readLock().unlock();
		}
	}

	public void write() {
		try {
		
			lock.writeLock().lock();
			System.out.println("获取写锁:" + Thread.currentThread().getName()
					+ " " + System.currentTimeMillis());
		} finally {
			lock.writeLock().unlock();
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
			service.write();
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
			service.write();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
