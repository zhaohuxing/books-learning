package com.sprint.ch02.method;

public class SynThreadB extends Thread {
	Syn syn;
	public SynThreadB(Syn syn) {
		this.syn = syn;
	}

	@Override
	public void run() {
		syn.method2();
	}
}
