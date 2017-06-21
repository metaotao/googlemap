package servergui;
import java.util.ArrayList;
import java.util.Map;

import rmi.HostService;

public class InCompleteTask{
	private ArrayList<String> disconnectList=IPDialog.instance().getDisconnectList();
	private Map<String,HostService> hostMap=IPDialog.instance().getMap();
	private static InCompleteTask inCompleteTask;

	public static InCompleteTask instance(){
		if(inCompleteTask==null){
			inCompleteTask=new InCompleteTask();
		}
		return inCompleteTask;
	}

	public InCompleteTask(){
		inCompleteTask=this;

	}

	public void addInCompleteTask(){				
		
		try{
			for(String key:hostMap.keySet()){					
				if(isEmpty()){
					if(disconnectList.size()==0){
						break;
					}
					HostService host=hostMap.get(key);
					String info=getOneInfo();	
					String[] split=info.split(",");
					int taskID=Integer.parseInt(split[0]);

					String county=split[1];
					host.getCountyInfo(county);						
					host.getTaskNum(taskID);
				}
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty(){
		if(disconnectList.size()==0){
			return false;
		}
		return true;
	}

	public String getOneInfo(){
		return disconnectList.remove(0);
	}
}