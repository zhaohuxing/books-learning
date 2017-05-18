public class StaticCode {
	public static void main(String[] args) {
		MyThread t1 = new MyThread();
		MyThread t2 = new MyThread();
		MyThread t3 = new MyThread();
		t1.start();
		t2.start();
		t3.start();
	}	
}

class MyObject {
	private static MyObject instance = null;
	private MyObject() {}
	static {
		instance = new MyObject();
	}
	public static MyObject getInstance() {
		return instance;
	}
}

class MyThread extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println(MyObject.getInstance().hashCode());
		}
	}
}
