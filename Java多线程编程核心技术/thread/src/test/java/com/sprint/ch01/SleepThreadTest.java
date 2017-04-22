package com.sprint.ch01;

import org.junit.Test;
public class SleepThreadTest {

	@Test
	public void testRun() throws InterruptedException {
		SleepThread thread = new SleepThread();
		thread.start();
		Thread.sleep(200);
		thread.interrupt();
	}
}
