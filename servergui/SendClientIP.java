package servergui;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import tool.SystemTool;

public class SendClientIP{
	private Socket socket;

	private String schedulerIP=SystemTool.instance().getSchedulerIP();
	private int port=SystemTool.instance().getCruisePort1();

	private ArrayList<String> list=IPDialog.instance().getList();

	public SendClientIP(){
		start();
	}

	public void start(){
		try{
			socket=new Socket(schedulerIP,port);
			sendObject("client");
			sendObject(list);
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