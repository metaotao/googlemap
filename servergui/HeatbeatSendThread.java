package servergui;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tool.*;

/**
* @author tao
* @version 1.0
*/

public class HeatbeatSendThread implements Runnable{
	
	
	private volatile boolean running=false;
	private Socket socket;	
	private String schedulerIP=SystemTool.instance().getSchedulerIP();
	private int port=SystemTool.instance().getCruisePort1();

	public void run(){
		// �Ѿ�����
		if (running){
			return;
		}

		try{
			socket=new Socket(schedulerIP,port);		
			socket.setKeepAlive(true);
			running=true;
			sendObject("jiankong");
			new Thread(new KeepAliveWatchDog()).start();
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

	public void stop(){
		if(running){
			running=false;
		}

		if(socket!=null){
			try{
				socket.close();
			} 
			catch(IOException e){
				e.printStackTrace();
			}
			finally{
				socket=null;
			}
		}

		System.out.println("����������ʧ�ܣ��رտͻ���");
	}

	class KeepAliveWatchDog implements Runnable{

		private long checkDelay=100;
		// ÿ��30�뷢��һ��������
		long keepAliveDelay = 30000;
		private long lastSendTime=System.currentTimeMillis();

		public void run(){
			while(running){
				if(System.currentTimeMillis()-lastSendTime>keepAliveDelay){
					
					try{
						HeatbeatSendThread.this.sendObject(new KeepAlive());
						System.out.println("�ѷ���������...");
						lastSendTime=System.currentTimeMillis();
					} 
					catch(Exception e){					
						e.printStackTrace();
						HeatbeatSendThread.this.stop();
					}					
				} 
				
				else{
					try{
						Thread.sleep(checkDelay);
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
						HeatbeatSendThread.this.stop();
					}
				}
			}
		}
	}
}