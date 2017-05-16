public class VariableIsolation {
	public static void main(String[] args) {
		try {
			ThreadA a = new ThreadA();
			ThreadB b = new ThreadB();
			a.start();
			b.start();
			for (int i = 0; i < 100; i++) {
				Tool.t1.set("Main:" + (i+1));
				System.out.println("Main get value:" + Tool.t1.get());
				Thread.sleep(200);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}

class Tool {
	public static ThreadLocal t1 = new ThreadLocal();
}

class ThreadA extends Thread {
	
	@Override
	public void run() {
		try {
			for (int i = 0; i < 100; i++) {
				Tool.t1.set("ThreadA" + (i+1));
				System.out.println("ThreadA get value: " + Tool.t1.get());
				Thread.sleep(200);
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
			for (int i = 0; i < 100; i++) {
				Tool.t1.set("ThreadB" + (i+1));
				System.out.println("ThreadB get value: " + Tool.t1.get());
				Thread.sleep(200);
			}		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
