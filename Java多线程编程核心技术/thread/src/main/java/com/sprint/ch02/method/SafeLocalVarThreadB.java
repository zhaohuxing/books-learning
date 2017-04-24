package com.sprint.ch02.method;

public class SafeLocalVarThreadB extends Thread {
	private SafeLocalVar localVar;

	public SafeLocalVarThreadB(SafeLocalVar localVar) {
		this.localVar = localVar;
	}

	@Override
	public void run() {
		localVar.add("b");
	}
}
