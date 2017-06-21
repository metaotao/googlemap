package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

import bean.InfoBean;
/**
* @author tao
* @version 1.0
*/

public interface HostService extends Remote{
	
	public void onConnected(String info) throws RemoteException,Exception;
	public void getCountyInfo(String info) throws RemoteException,Exception;
	public String getResult() throws RemoteException,Exception;
	public void getTaskNum(int num) throws RemoteException,Exception; 
}
