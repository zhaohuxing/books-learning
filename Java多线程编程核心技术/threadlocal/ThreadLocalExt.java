public class ThreadLocalExt extends ThreadLocal {

	//重写了该方法后不存在null值
	@Override
	protected Object initValue() {
		return new Date().getTime();
	}
}
