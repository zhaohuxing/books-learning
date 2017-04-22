package com.sprint.ch01;

public class SleepThread extends Thread {
	
	@Override
	public void run() {
		try {
			System.out.println("run start");
			Thread.sleep(200000);
			System.out.println("run end");
		} catch (InterruptedException e) {
			System.out.println("在沉睡中被停止!进入catch!" + this.isInterrupted());
			e.printStackTrace();
		}
	}
}
