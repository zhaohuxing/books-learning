package com.sprint.ch01;

import org.junit.Test;
public class ReturnThreadTest {
	
	@Test
	public void testRun() throws InterruptedException {
		ReturnThread thread = new ReturnThread();
		thread.start();
		Thread.sleep(2000);
		thread.interrupt();
	}
}
