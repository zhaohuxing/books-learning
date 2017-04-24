package com.sprint.ch02.method;

public class UnSafeInitVarThreadA extends Thread {
	private UnSafeInitVar var;
	public UnSafeInitVarThreadA(UnSafeInitVar var) {
		this.var = var;
	}

	@Override
	public void run() {
		var.add("a");
	}
}
