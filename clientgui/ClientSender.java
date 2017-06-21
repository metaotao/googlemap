package clientgui;
import java.io.IOException;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

import tool.*;

public class ClientSender{

	private DatagramSocket datagramSocket;
	private String ip;
	private String schedulerIP=SystemTool.instance().getSchedulerIP();
	private int jiankongPort=SystemTool.instance().getJiankongPort();

	public ClientSender(){
		try{			
			InetAddress address=InetAddress.getLocalHost(); 
			ip=address.getHostAddress();
			datagramSocket=new DatagramSocket(jiankongPort,address);
			
			sendInfo();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void sendInfo(){
		try{
			System.out.println("服务端启动...");
			byte[] buf=new byte[1024];
			DatagramPacket datagramPacket=new DatagramPacket(buf,buf.length);
			datagramSocket.receive(datagramPacket);
			String getIP=new String(buf,0,datagramPacket.getLength());
			System.out.println("调度端发送的数据是："+getIP);
			if(getIP.equals(ip)){
				new Thread(new CatchScreen(datagramSocket)).start();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				//input.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}