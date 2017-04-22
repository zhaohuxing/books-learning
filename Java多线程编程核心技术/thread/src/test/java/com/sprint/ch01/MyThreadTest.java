package com.sprint.ch01;

import org.junit.Test;
public class MyThreadTest {
	
	@Test
	public void testRun() {
		MyThread thread = new MyThread();
		thread.start();
	}
}
