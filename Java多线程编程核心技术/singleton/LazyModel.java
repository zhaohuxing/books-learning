//为了描述方便,将类都写在一个文件中的(不推荐使用这种) \
public class LazyModel {
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
	
	//volatile用于线程可见性
	private volatile static MyObject myObject = new MyObject();

	private MyObject() {
	
	}

	//使用双检测机制解决问题，即保证了不需要同步代码的异步执行性
	//又保证了单例的效果
	public static MyObject getInstance() {
		try {
			if (myObject != null) {
				
			} else {
				Thread.sleep(3000);
				synchronized (MyObject.class) {
					if (myObject == null) {
						myObject = new MyObject();
					}	
				}  
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return myObject;
	}

}

class MyThread extends Thread {
	
	@Override
	public void run() {
		System.out.println(MyObject.getInstance().hashCode());
	}
}

