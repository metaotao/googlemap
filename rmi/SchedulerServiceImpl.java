package rmi;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import servergui.*;

/**
* @author tao
* @version 1.0
*/

public class SchedulerServiceImpl extends UnicastRemoteObject implements SchedulerService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LeftCenterPane f;
	public SchedulerServiceImpl() throws RemoteException{
		super();
	}

	public SchedulerServiceImpl(LeftCenterPane f) throws RemoteException{
		this.f=f;
	}

	public void onConnected(String info){
		f.onConnected(info);		
	}

	public void getDownloadInfo(String info){
		f.getDownloadInfo(info);		
	}

	public void getProLatLng(ArrayList<String> list){
		f.getProLatLng(list);
	}

	public void getCountyLatLng(ArrayList<String> list){
		f.getCountyLatLng(list);
	}

	public void getTaskNum(int num){
		f.getTaskNum(num);
	}


}