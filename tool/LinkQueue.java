package tool;

public class LinkQueue {

	// 设定队列的阈值
	private static int threshold = 1000;
	private static Object signal = new Object();
	// 待访问的url集合
	private static URLQueue unVisitUrl = new URLQueue();

	// 获得URL队列
	public static URLQueue getUnVisitUrl() {
		return unVisitUrl;
	}

	// 未访问的URL出队列
	public static Object unVisitUrlDeQueue() {
		return unVisitUrl.deQueue();
	}

	// 保证每个URL只被访问一次
	public static synchronized void addUnvisitUrl(String url) {
		if (url != null && !url.trim().equals("") && !unVisitUrl.contains(url)) {
			unVisitUrl.enQueue(url);
		}
	}

	// 判断未访问的URL队列是否为空
	public static boolean isUnVisitUrlEmpty() {
		return unVisitUrl.isQueueEmpty();
	}
	
	//获取队列数目
	public static int getUrlNum(){
		return unVisitUrl.getUrlNum();
	}

	// 获取阈值
	public static int getThreshold() {
		return threshold;
	}
}
