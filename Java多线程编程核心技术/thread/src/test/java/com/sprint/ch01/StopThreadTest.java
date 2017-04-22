package com.sprint.ch01;

import org.junit.Test;
public class StopThreadTest {

	@Test
	public void testRun() throws InterruptedException {
		StopThread thread = new StopThread();
		thread.start();
		Thread.sleep(8000);
		thread.stop();
	}
}
