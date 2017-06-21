package bean;
import java.io.Serializable;
import rmi.HostService;
import rmi.SchedulerService;

public class InfoBean implements Serializable{
	private String closeInfo;
	private HostService host;
	private SchedulerService scheduler;
	private int ID;

	private String IP;
	public void setIP(String IP){
		this.IP=IP;
	}

	public String getIP(){
		return IP;
	}

	public void setCloseInfo(String closeInfo){
		this.closeInfo=closeInfo;
	}

	public String getCloseInfo(){
		return closeInfo;
	}

	public HostService getHost(){
		return host;
	}

	public void setHost(HostService host){
		this.host=host;
	}

	public SchedulerService getScheduler(){
		return scheduler;
	}

	public void setScheduler(SchedulerService scheduler){
		this.scheduler=scheduler;
	}

	public void setID(int ID){
		this.ID=ID;
	}

	public int getID(){
		return ID;
	}
}