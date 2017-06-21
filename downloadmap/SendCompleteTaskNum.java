package downloadmap;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import tool.SystemTool;
/**
* @author tao
* @version 1.0
*/

public class SendCompleteTaskNum{

	private Socket socket;

	private String serverIP=SystemTool.instance().getServerIP();
	private int port=SystemTool.instance().getCruisePort2();
	private int taskNum;

	public SendCompleteTaskNum(int taskNum){
		this.taskNum=taskNum;
		start();
	}

	public void start(){
		try{

			socket=new Socket(serverIP,port);
			sendObject("wancheng");
			String ip=InetAddress.getLocalHost().getHostAddress();
			sendObject(ip+" "+taskNum);
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
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(obj);
			oos.flush();
		}

		catch(IOException e){
			e.printStackTrace();
		}
	}

}