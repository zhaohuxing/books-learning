import java.util.concurrent.locks.ReentrantReadWriteLock;
public class WriteWriteLock {
	public static void main(String[] args) {
		Service service = new Service();
		ThreadA a = new ThreadA(service);
		ThreadB b = new ThreadB(service);
		a.start();
		b.start();
	} 
} 

class Service {
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public void write() {
		try {
			lock.writeLock().lock();
			System.out.println("获取写锁:" + Thread.currentThread().getName() + 
					" " + System.currentTimeMillis());
		} finally {
			lock.writeLock().unlock();
		}
	}
} 

class ThreadA extends Thread {
	Service service;
	ThreadA(Service service) {
		this.service = service;
	}

	@Override
	public void run() {
		service.write();
	}
}

class ThreadB extends Thread {
	Service service;
	ThreadB(Service service) {
		this.service = service;
	}

	@Override
	public void run() {
		service.write();
	}
}
