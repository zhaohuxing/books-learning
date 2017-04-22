package com.sprint.ch01;

import org.junit.Test;
public class ExceptionThreadTest {
	
	@Test
	public void testRun() throws InterruptedException {
		ExceptionThread thread = new ExceptionThread();
		thread.start();
		Thread.sleep(2000);
		thread.interrupt();
	}

}
