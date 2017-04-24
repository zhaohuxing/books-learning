package com.sprint.ch01;

import org.junit.Test;
public class StopThreadTest {
//因耗时常,不利于其他测试,先注释掉
//	@Test 
	public void testRun() throws InterruptedException {
		StopThread thread = new StopThread();
		thread.start();
		Thread.sleep(8000);
		thread.stop();
	}
}
