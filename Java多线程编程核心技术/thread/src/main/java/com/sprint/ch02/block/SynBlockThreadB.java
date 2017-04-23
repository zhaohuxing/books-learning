package com.sprint.ch02.block;

public class SynBlockThreadB extends Thread {
	private SynBlock obj;

	public SynBlockThreadB(SynBlock obj) {
		this.obj = obj;
	}

	@Override
	public void run() {
		obj.doLongTimeTask();
	}
}

