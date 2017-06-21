package scheduler;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.JScrollPane;

import bean.ClientBean;
import rmi.SchedulerService;
import sql.GetProInfo;

/**
* @author tao
* @version 1.0
*/

public class HeatbeatReceiveThread implements Runnable{
	private volatile boolean running=false;
	private ServerSocket serverSocket;
	//任务唯一标识
	private int ID;
	//任务IP
	private String ip;
	private ObjectInputStream input;
	private SchedulerService scheduler;
	private int taskNum;
	private Map<String,SchedulerService> map;
	public static ArrayList<String> list;
	private ArrayList<String> disconnectList=new ArrayList<String>();

	private int count;
	private int sum;

	private ClientBean clientBean=new ClientBean();
	
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
		//	serverSocket=new ServerSocket(port,5);
			while(running){
				Socket socket=serverSocket.accept();
				System.out.println("收到来自"+socket.getPort()+"端口的连接");
				input=new ObjectInputStream(socket.getInputStream());
				String str=(String)input.readObject();
				if(str.equals("jiankong")){
					new Thread(new DefaultSocket(socket)).start();
				}

				if(str.equals("wancheng")){
					receiveObject(socket);
				}
				
				if(str.equals("client")){
					receiveClientObject(socket);
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
			taskNum=(int)ois.readObject();

			System.out.println("母蜘蛛返回任务标识为："+taskNum);

			if(ShowTaskPane.instance().isEmpty()){
				map=IPDialog.instance().getMap();
				scheduler=map.get(ip);
				System.out.println("任务："+taskNum+" 完成！");
				String pro=ShowTaskPane.instance().getOneProInfo();
				scheduler.getDownloadInfo(pro);
				scheduler.getProLatLng(GetProInfo.instance().getProLatLng(pro));
				scheduler.getCountyLatLng(GetProInfo.instance().getCountyLatLng(pro));
				int taskID=ShowTaskPane.instance().getOneTaskNum();
				scheduler.getTaskNum(taskID);
				sum+=1;
				EastPane.instance().setCompleteTaskText(sum+"");
				IPDialog.instance().getMap3().put(taskID, pro);
			}
			else{
				System.out.println("所有地图下载完毕！");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void receiveClientObject(Socket socket){
		try{
			ObjectInputStream input=new ObjectInputStream(socket.getInputStream());
			list=(ArrayList)input.readObject();
			clientBean.setList(list);
			System.out.println(list);
			CenterPane.instance().addContentPane(list);
			MonitorPane.instance().add(new JScrollPane(new IPTreePane(this)),BorderLayout.WEST);
			MonitorPane.instance().updateUI();
			MonitorPane.instance().repaint();
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
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

		System.out.println("心跳包接收失败，关闭调度端");
	}

	class DefaultSocket implements Runnable{
		private Socket socket;
		private boolean flag=true;
		private long lastReceiveTime;
		
		// 如果30秒内没收到客户端的信息，就自动端口连接
		// 但是这里检测用的是31秒
		private long receiveTimeDelay=35000;
		private long checkDelay=100;
		
		private File log;
		private PrintWriter pw;

		public DefaultSocket(Socket socket){
			this.socket=socket;
			try{
				log=new File("log//"+"母蜘蛛"+ip+".log");
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
						if(in.available()>0){
							// 接收心跳包
							ObjectInputStream ois=new ObjectInputStream(in);
							Object obj=ois.readObject();	
							if(taskNum<=IPDialog.instance().getSum()){
								pw.println("当前任务标识为："+ID+",  当前任务IP为："+ip+",  当前时间为："+obj+
									",  30秒内母蜘蛛运行正常");
							}
							if(taskNum>IPDialog.instance().getSum()){
								pw.println("当前任务标识为："+taskNum+",  当前任务IP为："+ip+",  当前时间为："+obj+
									",  30秒内母蜘蛛运行正常");
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
			System.out.println("未收到心跳包.........母蜘蛛主动关闭");
			pw.println("接收心跳包失败，母蜘蛛出现异常现象，出现时间为："+
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			count+=1;
			disconnectList.add(ip);

			EastPane.instance().setDeadText(count+"");
			EastPane.instance().setCompleteTaskText((IPDialog.instance().getSum()-count)+"");
			if(flag){
				flag=false;
			}
			if(socket!=null){
				System.out.println("端口："+socket.getPort());
				try{
					socket.close();
				} 
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	public void setList(ArrayList<String> list){
		this.list=list;
	}

	public ArrayList<String> getList(){
		return list;
	}

	public void setDisconnectList(ArrayList<String> disconnectList){
		this.disconnectList=disconnectList;
	}

	public ArrayList<String> getDisconnectList(){
		return disconnectList;
	}

}
