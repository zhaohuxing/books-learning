package com.sprint.ch02.method;

public class MoreObjMoreLockThreadA extends Thread {
	private MoreObjMoreLock obj;

	public MoreObjMoreLockThreadA(MoreObjMoreLock obj) {
		this.obj = obj;
	}
	
	@Override
	public void run() {
		obj.add("a");
	}
}
