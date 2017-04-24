package com.sprint.ch02.method;

import org.junit.Test;
public class MoreObjMoreLockTest {

	@Test
	public void testRun() {
		MoreObjMoreLock obj = new MoreObjMoreLock();
		MoreObjMoreLockThreadA a = new MoreObjMoreLockThreadA(obj);
		a.start();
		MoreObjMoreLock obj1 = new MoreObjMoreLock();
		MoreObjMoreLockThreadB b = new MoreObjMoreLockThreadB(obj1);
		b.start();
	}
}
