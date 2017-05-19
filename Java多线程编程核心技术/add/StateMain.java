public class StateMain {
	public static void main(String[] args) {
		try {
			
			MyThread thread = new MyThread();
			System.out.println("main方法中的状态:" + thread.getState());
			Thread.sleep(1000);
			thread.start();
			Thread.sleep(1000);
			System.out.println("main方法中的状态:" + thread.getState());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}

class MyThread extends Thread {
	public MyThread() {
		System.out.println("构造方法中的状态:" + Thread.currentThread().getState());
	}

	@Override
	public void run() {
		System.out.println("run方法中的状态:" + Thread.currentThread().getState());
	}
}
