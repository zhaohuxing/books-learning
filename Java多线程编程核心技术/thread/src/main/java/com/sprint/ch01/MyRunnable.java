package com.sprint.ch01;

public class MyRunnable implements Runnable {
	
	@Override
	public void run() {
		System.out.println("多线程之Runnable实现");
	}
}
