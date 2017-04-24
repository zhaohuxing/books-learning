package com.sprint.ch02.method;

public class SafeLocalVarThreadA extends Thread {
	private SafeLocalVar localVar;

	public SafeLocalVarThreadA(SafeLocalVar localVar) {
		this.localVar = localVar;
	}

	@Override
	public void run() {
		localVar.add("a");
	}
}
