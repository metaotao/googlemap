package tool;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Collections;

/**
 * ����һ��url����
 */
public class URLQueue {
	//private List<Object> queue = Collections.synchronizedList(new LinkedList<Object>());
	private LinkedList queue = new LinkedList();

	// �����
	public synchronized void enQueue(Object t) {
		((LinkedList<Object>) queue).addLast(t);
	}

	// ������
	public Object deQueue() throws NoSuchElementException {
		return ((LinkedList<Object>) queue).removeFirst();
	}

	// �ж϶����Ƿ�Ϊ��
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// �ж϶������Ƿ����t
	public boolean contains(Object t) {
		return queue.contains(t);
	}
	
	//��ȡ���е���Ŀ
	public int getUrlNum(){
		return queue.size();
	}
}