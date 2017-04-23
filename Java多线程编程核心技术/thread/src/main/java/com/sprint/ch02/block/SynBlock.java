package com.sprint.ch02.block;

public class SynBlock {
	private String getData1;
	private String getData2;

	public void doLongTimeTask() {
		try {
			
			System.out.println("begin task");
			Thread.sleep(3000);
			String data1 = "长时间处理任务后从远程返回的值 1 threadName=" + Thread.currentThread().getName();
			String data2 = "长时间处理任务后从远程返回的值 2 threadName=" + Thread.currentThread().getName();
			
			synchronized(this) {
				getData1 = data1;
				getData2 = data2;
			}
			System.out.println(getData1);
			System.out.println(getData2);
			System.out.println("begin end");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
