package scheduler;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import tool.SystemTool;


/**
* @author tao
* @version 1.0
*/

public class ServerThread implements Runnable{
	private BufferedImage image;

	private volatile boolean running=false;
//	private ServerSocket serverSocket;
	private DatagramSocket socket;	
	private ScreenPane screenPane;
	private String ip;
	private int port=SystemTool.instance().getJiankongPort();

	public ServerThread(String ip){
		this.ip=ip;
		try{
			socket=new DatagramSocket();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}	

	public void run(){
		if(running){
			return;
		}
		running=true;
		try{			
			System.out.println("¿ªÊ¼");
			getInfo(ip);				
			new Thread(new ScreenReceiver(socket,screenPane)).start();						
		}
		catch(Exception e){
		//	e.printStackTrace();
		}
		
	}

	public void getInfo(String ip){		
		try{
			byte[] buf=ip.getBytes();
			DatagramPacket packet=new DatagramPacket(buf,buf.length,
				InetAddress.getByName(ip),port);
			socket.send(packet);

			screenPane=ScreenDialog.instance().getScreenPane();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}