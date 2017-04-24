package com.sprint.ch02.method;

public class MoreObjMoreLock {
	private int num = 0;
	public synchronized void add(String username) {
		try {
			if (username.equals("a")) {
				num = 100;
				System.out.println("a set over!");
				Thread.sleep(2000);
			} else {
				num = 200;
				System.out.println("b set over!");
			}
			System.out.println(username + " num = " + num);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
