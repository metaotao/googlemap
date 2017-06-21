package rmi;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import java.rmi.registry.LocateRegistry;

import java.net.InetAddress;
import java.net.MalformedURLException;

import clientgui.ServerPane;
import servergui.IPDialog;
import tool.SystemTool;
/**
* @author tao
* @version 1.0
*/

public class RMIServerClient{

	private String ipAddress=null;
	private clientgui.MainFrame f;
	private IPDialog ipDialog;

	public RMIServerClient(){
	}

	public RMIServerClient(clientgui.MainFrame f){
		this.f=f;
	}

	public RMIServerClient(IPDialog ipDialog){
		this.ipDialog=ipDialog;
	}
	
	public HostService createClient(String ip){
		HostService host=null;
		int port=SystemTool.instance().getServerPort();
		try{
		//	System.setSecurityManager(new RMISecurityManager());//ע�ᰲȫ������
			System.out.println("client:"+"rmi://"+ip+":"+port+"/host");
			host=(HostService)Naming.lookup("rmi://"+ip+":"+port+"/host");
			System.out.println("��ÿͻ���Զ�̶���.....");
			
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

	public HostService createServer1(){
		HostService host=null;
		int port=SystemTool.instance().getServerPort();
		try{
			ipAddress=InetAddress.getLocalHost().getHostAddress();
			host=new HostServiceImpl(f);
			LocateRegistry.createRegistry(port);
			Naming.bind("rmi://"+ipAddress+":"+port+"/host",host);
			System.out.println("Զ�̷������󶨳ɹ���");
			
		}
		catch(RemoteException e){
			System.out.println("����Զ�̶�����쳣��");
			ServerPane.instance().getConnection_label().setText("��");
			e.printStackTrace();
		}
		catch(AlreadyBoundException e){
			System.out.println("�����ظ����쳣��");
			ServerPane.instance().getConnection_label().setText("��");
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			System.out.println("����URL�쳣!");
			ServerPane.instance().getConnection_label().setText("��");
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("���������쳣��");
			ServerPane.instance().getConnection_label().setText("��");
			e.printStackTrace();
		}
		return host;
	}

	public void createServer2(){

		HostService host=null;
		int port=SystemTool.instance().getServerPort();
		try{

			ipAddress=InetAddress.getLocalHost().getHostAddress();
			host=new HostServiceImpl();
			LocateRegistry.createRegistry(port);
			Naming.bind("rmi://"+ipAddress+":"+port+"/host",host);
			System.out.println("Զ�̷������󶨳ɹ���");
		}
		catch(RemoteException e){
			System.out.println("����Զ�̶�����쳣��");
			e.printStackTrace();
		}
		catch(AlreadyBoundException e){
			System.out.println("�����ظ����쳣��");
			e.printStackTrace();
		}
		catch(MalformedURLException e){
			System.out.println("����URL�쳣!");
			e.printStackTrace();
		}
		catch(Exception e){
			System.out.println("���������쳣��");
			e.printStackTrace();
		}
	}
}