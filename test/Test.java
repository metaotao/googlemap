package test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

	public static void main(String args[]) throws InterruptedException {
//		ExecutorService exe = Executors.newFixedThreadPool(5);
//		for (int i = 1; i <= 15; i++) {
//			exe.execute(new SubThread(i));
//		}
//		exe.shutdown();
//
//		while (true) {
//			if (exe.isTerminated()) {
//				System.out.println("½áÊøÁË£¡");
//				break;
//			}
//			Thread.sleep(200);
//		}
		
		String path="E:\\1\\2\\3\\";
		String[] split=path.split("\\\\");
		String name=split[1]+"_"+split[2]+"_"+split[3];
		System.out.println(name);
		
	}
}

// class SubThread extends Thread {
//
// private int i;
//
// public SubThread(int i) {
// this.i = i;
// }
//
// @Override
// public void run() {
// System.out.println(i);
// }
// }