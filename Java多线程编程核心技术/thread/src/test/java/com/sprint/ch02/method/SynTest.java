package com.sprint.ch02.method;

import org.junit.Test;
public class SynTest {
	
	@Test
	public void testRun() {
		Syn syn = new Syn();
		SynThreadA a = new SynThreadA(syn);
		a.start();
		SynThreadB b = new SynThreadB(syn);
		b.start();
	}
}
