package com.sprint.ch01;

import org.junit.Test;
public class ReturnThreadTest {
	
//因耗时常,不利于其他测试,先注释掉
//	@Test
	public void testRun() throws InterruptedException {
		ReturnThread thread = new ReturnThread();
		thread.start();
		Thread.sleep(2000);
		thread.interrupt();
	}
}
