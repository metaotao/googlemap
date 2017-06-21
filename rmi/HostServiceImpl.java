package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import servergui.IPDialog;

/**
* @author tao
* @version 1.0
*/
public class HostServiceImpl extends UnicastRemoteObject implements HostService{
	private clientgui.MainFrame f;
	private IPDialog ipDialog;
	public HostServiceImpl() throws RemoteException{
		super();
	}

	public HostServiceImpl(IPDialog ipDialog) throws RemoteException{
		this.ipDialog=ipDialog;
	}
	
	public HostServiceImpl(clientgui.MainFrame f) throws RemoteException{
		this.f=f;
	}

	public void onConnected(String info){
		f.onConnected(info);
	}

	public void getCountyInfo(String info){
		f.getCountyInfo(info);
	}

	public String getResult(){
		return null;
	}

	public void getTaskNum(int num){
		f.getTaskNum(num);
	}
}