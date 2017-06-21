package servergui;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tool.SystemTool;

public class SendCompleteTaskNum{
	private Socket socket;

	private String schedulerIP=SystemTool.instance().getSchedulerIP();
	private int port=SystemTool.instance().getCruisePort1();
	private int taskNum;

	public SendCompleteTaskNum(int taskNum){
		this.taskNum=taskNum;
		start();
	}

	public void start(){
		try{
			socket=new Socket(schedulerIP,port);
			sendObject("wancheng");
			sendObject(taskNum);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendObject(Object obj){
		try{
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(obj);
			oos.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}