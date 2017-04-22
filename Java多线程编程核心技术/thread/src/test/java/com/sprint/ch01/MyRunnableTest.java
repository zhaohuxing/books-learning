package com.sprint.ch01;

import org.junit.Test;
public class MyRunnableTest {

	@Test
	public void testRun() {
		Thread thread = new Thread(new MyRunnable());
		thread.start();
	}
}
