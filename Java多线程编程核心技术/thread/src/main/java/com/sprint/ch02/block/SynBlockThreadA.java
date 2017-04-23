package com.sprint.ch02.block;

public class SynBlockThreadA extends Thread {
	private SynBlock obj;

	public SynBlockThreadA(SynBlock obj) {
		this.obj = obj;
	}

	@Override
	public void run() {
		obj.doLongTimeTask();
	}
}

