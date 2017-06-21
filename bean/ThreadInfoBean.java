package bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadInfoBean{
	private ExecutorService exec;

	public void setExec(ExecutorService exec){
		this.exec=exec;
	}

	public ExecutorService getExec(){
		return exec;
	}

}