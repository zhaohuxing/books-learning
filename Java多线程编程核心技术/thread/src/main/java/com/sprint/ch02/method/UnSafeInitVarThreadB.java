package com.sprint.ch02.method;

public class UnSafeInitVarThreadB extends Thread {
	private UnSafeInitVar var;
	public UnSafeInitVarThreadB(UnSafeInitVar var) {
		this.var = var;
	}
	
	@Override
	public void run() {
		var.add("b");
	}
}
