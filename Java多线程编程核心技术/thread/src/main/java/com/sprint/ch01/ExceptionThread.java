package com.sprint.ch01;

public class ExceptionThread extends Thread {
	@Override
	public void run() {
		try {
			for (int i = 0; i < 500000; i++) {
				if (this.interrupted()) {
					System.out.println("已经是停止状态了!我要退出");
					throw new InterruptedException();
				}
				System.out.println("i=" + (i+1));
			}
		} catch (InterruptedException e) {
			System.out.println("进ExceptionThread.java类run方法中的catch了!");	
			e.printStackTrace();
		}
	}
}
