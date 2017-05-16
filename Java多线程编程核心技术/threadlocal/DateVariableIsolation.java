import java.util.Date;
public class DateVariableIsolation {
	public static void main(String[] args) {
		try {
			ThreadA a = new ThreadA();
			a.start();
			Thread.sleep(1000);
			ThreadB b = new ThreadB();
			b.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}

class Tool {
	public static ThreadLocal<Date> t1 = new ThreadLocal<>();
}

class ThreadA extends Thread {
	
	@Override
	public void run() {
		try {
			for (int i = 0; i < 20; i++) {
				if (Tool.t1.get() == null) {
					Tool.t1.set(new Date());
				}
				System.out.println("A: " + Tool.t1.get().getTime());
				Thread.sleep(100);
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ThreadB extends Thread {
	
	@Override
	public void run() {
		try {
			for (int i = 0; i < 20; i++) {
				if (Tool.t1.get() == null) {
					Tool.t1.set(new Date());
				}
				System.out.println("B: " + Tool.t1.get().getTime());
				Thread.sleep(100);
			}	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
