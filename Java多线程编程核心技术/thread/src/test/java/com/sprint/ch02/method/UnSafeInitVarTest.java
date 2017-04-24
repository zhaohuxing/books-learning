package com.sprint.ch02.method;

import org.junit.Test;
public class UnSafeInitVarTest {

	@Test
	public void testRun() {
		UnSafeInitVar var = new UnSafeInitVar();
		UnSafeInitVarThreadA a = new UnSafeInitVarThreadA(var);
		a.start();
		UnSafeInitVarThreadB b = new UnSafeInitVarThreadB(var);
		b.start();
	}
}
