package com.sprint.ch02.block;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
public class SynBlockTest {
	
	@Test
	public void testRun() {
		SynBlock obj = new SynBlock();
		SynBlockThreadA a = new SynBlockThreadA(obj);
		a.start();
		SynBlockThreadB b = new SynBlockThreadB(obj);
		b.start();
	}
}
