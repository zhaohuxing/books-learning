package com.sprint.ch01;

import org.junit.Test;
public class ThreadMethodsTest {
	
	@Test
	public void testThreadMethods() throws InterruptedException {
		MyThread thread = new MyThread();
		thread.start();
		System.out.println("currentThread.name:" + Thread.currentThread().getName());
		System.out.println("isAlive:" + thread.isAlive());
		Thread.sleep(3000); //休眠3秒
		System.out.println("getId:" + thread.getId());
		Thread.yield();
	}
}
