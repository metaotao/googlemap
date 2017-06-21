package servergui;

import java.util.Map;
import java.util.HashMap;

public class ClientDisconnect{
	private static ClientDisconnect clientDisconnect;
	
	private Map<Integer,String> map=new HashMap<Integer,String>();

	public static ClientDisconnect instance(){
		if(clientDisconnect==null){
			clientDisconnect=new ClientDisconnect();
		}
		return clientDisconnect;
	}
	
	public ClientDisconnect(){
		clientDisconnect=this;
	}
	public void setMap(Map<Integer,String> map){
		this.map=map;
	}

	public Map<Integer,String> getMap(){
		return map;
	}



	
}