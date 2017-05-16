public class Run {
	public static ThreadLocal t1 = new ThreadLocal();
	public static void main(String[] args) {
		if (t1.get() == null) {
			t1.set("我不是空值");
		}	
		System.out.println(t1.get());
		System.out.println("获取数据，不像栈那样弹出来" + t1.get());
	}
}
