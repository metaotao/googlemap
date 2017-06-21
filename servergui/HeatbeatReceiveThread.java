package servergui;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import rmi.HostService;
import tool.InfoTool;
/**
* @author tao
* @version 1.0
*/

public class HeatbeatReceiveThread implements Runnable{
	private volatile boolean running=false;
	private ServerSocket serverSocket;
	
	private ObjectInputStream input;

	private HostService host;
	private int taskNum;
	private Map<String,HostService> map;
	
	private Map<String,ShowTaskPane> serverMap;
	private Map<String,HostService> serviceMap;
	private ShowTaskPane showTaskPane;
	private String ip;
	private int ID;
	private int taskID;
	private String county;
	private String obj;
	private String[] split;
	private ArrayList<Integer> completeList=new ArrayList<Integer>();
	private int counter=0;
	private static HeatbeatReceiveThread thread;
	public static HeatbeatReceiveThread instance(){
		if(thread==null){
			thread=new HeatbeatReceiveThread();
		}
		return thread;
	}

	
	public HeatbeatReceiveThread(){
		thread=this;
	}

	public HeatbeatReceiveThread(ServerSocket serverSocket,String ip,int ID){
		this.serverSocket=serverSocket;	
		this.ip=ip;
		this.ID=ID;
	}

	public void run(){
		if(running){
			return;
		}
		running=true;
		
		try{			
			while(running){
				Socket socket=serverSocket.accept();
				System.out.println("�յ�����"+socket.getPort()+"�˿ڵ�����");

				input=new ObjectInputStream(socket.getInputStream());
				String str=(String)input.readObject();
				if(str.equals("jiankong")){
					new Thread(new DefaultSocket(socket)).start();
				}
				if(str.equals("wancheng")){
					receiveObject(socket);
					counter+=1;
			
					completeList.add(counter);
					System.out.println("�����������"+completeList.size());
				}
			
				try{
					Thread.sleep(1000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			HeatbeatReceiveThread.this.stop();
		}
		catch(Exception e){
			e.printStackTrace();
			HeatbeatReceiveThread.this.stop();
		}
	}

	public void receiveObject(Socket socket){
		try{
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			obj=(String)ois.readObject();
			split=obj.split(" ");
			taskNum=Integer.parseInt(split[1]);
			System.out.println("���������ʶΪ��"+taskNum);
			System.out.println("ipΪ��"+split[0]+"����"+taskNum+"  ��ɣ�");		

			if(LeftCenterPane.instance().isEmpty()){
				map=IPDialog.instance().getMap();
				host=map.get(split[0]);				
				
				taskID=LeftCenterPane.instance().getOneTaskNum();
				county=LeftCenterPane.instance().getOneCountyLatLng();
				System.out.println(county);

				IPDialog.instance().getMap3().put(taskID,county);				
				IPDialog.instance().getMap2().put(split[0],taskID);

				host.getTaskNum(taskID);
				host.getCountyInfo(county);				
				
			}
			
			else if(IPDialog.instance().getDisconnectList().size()!=0){
				
				if(InCompleteTask.instance().isEmpty()){
					System.out.println("δ����������·��䣡����");
					InCompleteTask.instance().addInCompleteTask();
				}
			}
			
			else{
				System.out.println("��֩����������Ϊ��"+LeftCenterPane.instance().getListSum());
				new SendCompleteTaskNum(InfoTool.serverCompleteTask);
				InfoTool.serverCompleteTask=0;
				counter=0;
				completeList.clear();
			}
			
		}

		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setTaskNum(int taskNum){
		this.taskNum=taskNum;
	}

	public int getTaskNum(){
		return taskNum;
	}

	public void setTaskID(int taskID){
		this.taskID=taskID;
	}

	public int getTaskID(){
		return taskID;
	}

	public void setCounty(String county){
		this.county=county;
	}

	public String getCounty(){
		return county;
	}

	public void stop(){
		if(running){
			running=false;
		}
		if(serverSocket!=null){
			try{
				serverSocket.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		System.out.println("����������ʧ�ܣ��رշ����");
	}

	class DefaultSocket implements Runnable{
		private Socket socket;
		private boolean flag=true;
		private long lastReceiveTime;
		
		// ���30����û�յ��ͻ��˵���Ϣ�����Զ��Ͽ�����
		// �����������õ���31��
		private long receiveTimeDelay=35000;
		private long checkDelay=100;
		
		private File log;
		private PrintWriter pw;

		public DefaultSocket(Socket socket){
			this.socket=socket;
			try{
				log=new File("log//"+"��֩��"+ip+".log");
				pw=new PrintWriter(new FileWriter(log,true),true);
			}
			catch(IOException e){
				e.printStackTrace();
			}

			lastReceiveTime=System.currentTimeMillis();
		}

		public void run(){			
			while(running&&flag){
				if(System.currentTimeMillis()-lastReceiveTime>receiveTimeDelay){					
					shutdownSocket();
				} 

				else{
					try{						
						InputStream in=socket.getInputStream();
						//IPDialog.instance().getMap2();
						if(in.available()>0){
							// ����������
							ObjectInputStream ois=new ObjectInputStream(in);
							Object object=ois.readObject();
							if(taskNum<=IPDialog.instance().getIPSum()){
								pw.println("��ǰ�����ʶΪ��"+ID+",  ��ǰ����IPΪ��"+ip+",  ��ǰʱ��Ϊ��"+object+
									",  30������֩����������");	
							}

							if(taskNum>IPDialog.instance().getIPSum()){
								pw.println("��ǰ�����ʶΪ��"+taskNum+",  ��ǰ����IPΪ��"+ip+",  ��ǰʱ��Ϊ��"+object+
									",  30������֩����������");	
							}

							lastReceiveTime=System.currentTimeMillis();
						} 
						else{
							Thread.sleep(checkDelay);
							
						}
					} 
					catch(IOException e){
						e.printStackTrace();
						shutdownSocket();
					}
					catch(Exception e){
						e.printStackTrace();
						shutdownSocket();
					}
				}
			}
		}

		public void shutdownSocket(){
			System.out.println("δ�յ�������.........�ͻ��������ر�");
			serviceMap=IPDialog.instance().getMap();
			serviceMap.remove(ip);

			pw.println("����������ʧ�ܣ���֩������쳣���󣬳���ʱ��Ϊ��"+
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			serverMap=IPDialog.instance().getMap4();
			showTaskPane=serverMap.get(ip);
			showTaskPane.setLinkText("��");
			showTaskPane.setStatusText("ֹͣ����");

			if(taskID<=IPDialog.instance().getIPSum()){
				
				showTaskPane.setBreakAreaText(IPDialog.instance().getMap3().get(ID));
				String county=IPDialog.instance().getMap3().get(ID);
				IPDialog.instance().getDisconnectList().add(ID+","+county);
				
			}
			if(taskID>IPDialog.instance().getIPSum()){
				
				showTaskPane.setBreakAreaText(IPDialog.instance().getMap3().get(taskID));
				String county1=IPDialog.instance().getMap3().get(taskID);
				IPDialog.instance().getDisconnectList().add(taskID+","+county1);
			}
		
			if(flag){
				flag=false;
			}
			if(socket!=null){
				System.out.println("�˿ڣ�"+socket.getPort());
				try{
					socket.close();
				} 
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

}
