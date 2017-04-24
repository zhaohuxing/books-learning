package com.sprint.ch02.method;

public class SynThreadA extends Thread {
	Syn syn;
	public SynThreadA(Syn syn) {
		this.syn = syn;
	}

	@Override
	public void run() {
		syn.method1();	
	}
}
