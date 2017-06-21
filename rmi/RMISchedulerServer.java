package rmi;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;

import java.net.InetAddress;
import java.net.MalformedURLException;
import servergui.LeftCenterPane;
import servergui.ServerPane;
import scheduler.IPDialog;
import tool.SystemTool;

/**
* @author tao
* @version 1.0
*/

public class RMISchedulerServer{
	
	private LeftCenterPane f;
	private IPDialog ipDialog;
	private String ipAddress=null;

	public RMISchedulerServer(){
	}
	
	public RMISchedulerServer(IPDialog ipDialog){
		this.ipDialog=ipDialog;
	}

	public RMISchedulerServer(LeftCenterPane f){
		this.f=f;
	}

	public SchedulerService createClient(String ip){
		SchedulerService host=null;
		int port=SystemTool.instance().getLocalPort();
		try{
			System.out.println("获得母蜘蛛信息："+"rmi://"+ip+":"+port+"/host");
			host=(SchedulerService)Naming.lookup("rmi://"+ip+":"+port+"/host");
			System.out.println("获得母蜘蛛远程对象....");
		}
		catch(NotBoundException e){
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			e.printStackTrace();
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		return host;
	}

	public SchedulerService createServer1(){
		SchedulerService host=null;
		
		int port=SystemTool.instance().getLocalPort();
		try{
			ipAddress=InetAddress.getLocalHost().getHostAddress();
			host=new SchedulerServiceImpl(f);
			LocateRegistry.createRegistry(port);
			Naming.bind("rmi://"+ipAddress+":"+port+"/host",host);
			System.out.println("远程服务对象绑定成功！");
			
		}
		catch(RemoteException e){
			System.out.println("创建远程对象绑定异常！");
			ServerPane.instance().getLink().setText("否");
			e.printStackTrace();
		}
		catch(AlreadyBoundException e){
			System.out.println("发现重复绑定异常！");
			ServerPane.instance().getLink().setText("否");
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			System.out.println("发生URL异常!");
			ServerPane.instance().getLink().setText("否");
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("出现其他异常！");
			ServerPane.instance().getLink().setText("否");
			e.printStackTrace();
		}
		return host;
	}

	public void createServer2(){
		try{
			ipAddress=InetAddress.getLocalHost().getHostAddress();
			int port=SystemTool.instance().getLocalPort();
			SchedulerService host=new SchedulerServiceImpl();
			LocateRegistry.createRegistry(port);
			Naming.bind("rmi://"+ipAddress+":"+port+"/host",host);
			System.out.println("远程服务对象绑定成功！");
		}
		catch(RemoteException e){
			System.out.println("创建远程对象绑定异常！");
			e.printStackTrace();
		}
		catch(AlreadyBoundException e){
			System.out.println("发现重复绑定异常！");
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			System.out.println("发生URL异常!");
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("出现其他异常！");
			e.printStackTrace();
		}
	}
}