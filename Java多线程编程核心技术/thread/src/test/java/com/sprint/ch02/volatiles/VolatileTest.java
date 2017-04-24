package com.sprint.ch02.volatiles;

import org.junit.Test;
public class VolatileTest {

	@Test
	public void testRun() throws InterruptedException {
		Volatile v = new Volatile();
		Thread thread = new Thread(v);
		thread.start();
		Thread.sleep(1000);
		v.setRunning(false);

	}
}
