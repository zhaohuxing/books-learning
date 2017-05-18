//为了描述方便,将类都写在一个文件中的(不推荐使用这种) 
public class InnerClass {
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

	//在未调用前已经被是加载，并实例
	private static class MyObjectHandler {
		private static MyObject myObject =  new MyObject();
	}

	private MyObject() {
	}

	public static MyObject getInstance() {
		//因为 getInstance没有同步，所以存在线程安全问题
		return MyObjectHandler.myObject;
	}

}

class MyThread extends Thread {
	
	@Override
	public void run() {
		System.out.println(MyObject.getInstance().hashCode());
	}
}

