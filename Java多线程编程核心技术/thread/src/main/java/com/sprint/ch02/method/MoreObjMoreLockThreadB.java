package com.sprint.ch02.method;

//哇 文件名有点长了
public class MoreObjMoreLockThreadB extends Thread {
	private MoreObjMoreLock obj;

	public MoreObjMoreLockThreadB(MoreObjMoreLock obj) {
		this.obj = obj;
	}
	
	@Override
	public void run() {
		obj.add("b");
	}
		
}
