package tool;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Collections;

/**
 * 定义一个url队列
 */
public class URLQueue {
	//private List<Object> queue = Collections.synchronizedList(new LinkedList<Object>());
	private LinkedList queue = new LinkedList();

	// 入队列
	public synchronized void enQueue(Object t) {
		((LinkedList<Object>) queue).addLast(t);
	}

	// 出队列
	public Object deQueue() throws NoSuchElementException {
		return ((LinkedList<Object>) queue).removeFirst();
	}

	// 判断队列是否为空
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	// 判断队列中是否包含t
	public boolean contains(Object t) {
		return queue.contains(t);
	}
	
	//获取队列的数目
	public int getUrlNum(){
		return queue.size();
	}
}