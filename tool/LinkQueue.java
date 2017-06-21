package tool;

public class LinkQueue {

	// �趨���е���ֵ
	private static int threshold = 1000;
	private static Object signal = new Object();
	// �����ʵ�url����
	private static URLQueue unVisitUrl = new URLQueue();

	// ���URL����
	public static URLQueue getUnVisitUrl() {
		return unVisitUrl;
	}

	// δ���ʵ�URL������
	public static Object unVisitUrlDeQueue() {
		return unVisitUrl.deQueue();
	}

	// ��֤ÿ��URLֻ������һ��
	public static synchronized void addUnvisitUrl(String url) {
		if (url != null && !url.trim().equals("") && !unVisitUrl.contains(url)) {
			unVisitUrl.enQueue(url);
		}
	}

	// �ж�δ���ʵ�URL�����Ƿ�Ϊ��
	public static boolean isUnVisitUrlEmpty() {
		return unVisitUrl.isQueueEmpty();
	}
	
	//��ȡ������Ŀ
	public static int getUrlNum(){
		return unVisitUrl.getUrlNum();
	}

	// ��ȡ��ֵ
	public static int getThreshold() {
		return threshold;
	}
}
