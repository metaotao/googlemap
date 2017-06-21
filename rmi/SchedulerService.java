package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
/**
* @author tao
* @version 1.0
*/

public interface SchedulerService extends Remote{

	public void onConnected(String info) throws RemoteException,Exception;
	/*public void getList(ArrayList<String> list) throws RemoteException,Exception;
	public String getResult() throws RemoteException,Exception;
	public void getObjAmount(int amount) throws RemoteException,Exception;*/
	public void getDownloadInfo(String info) throws RemoteException,Exception; 
	public void getProLatLng(ArrayList<String> list) throws RemoteException,Exception; 
	public void getCountyLatLng(ArrayList<String> list) throws RemoteException,Exception; 
	public void getTaskNum(int num) throws RemoteException,Exception; 
}