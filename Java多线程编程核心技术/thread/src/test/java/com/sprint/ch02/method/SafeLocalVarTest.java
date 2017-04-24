package com.sprint.ch02.method;

import org.junit.Test;
public class SafeLocalVarTest {

	@Test
	public void testRun() {
		SafeLocalVar var = new SafeLocalVar();
		SafeLocalVarThreadA a = new SafeLocalVarThreadA(var);
		a.start();
		SafeLocalVarThreadB b = new SafeLocalVarThreadB(var);
		b.start();
	}
}
