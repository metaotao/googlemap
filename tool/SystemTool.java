package tool;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
* @author tao
* @version 1.0
*/
public class SystemTool{
	
	private String schedulerIP;
	private int schedulerPort;

	private int cruisePort1;
	private int cruisePort2;
	private int jiankongPort;

	private String serverIP;
	private int serverPort;
	private String localIP;
	private int localPort;

	private static SystemTool systemTool;
	public static SystemTool instance(){
		if(systemTool==null){
			systemTool=new SystemTool();
		}
		return systemTool;
	}

	public SystemTool(){
		systemTool=this;
		FileInputStream in=null;
		try{
			in=new FileInputStream("data/data.properties");
		}
		catch(IOException e){
			e.printStackTrace();
		}

		Properties p=new Properties();
		try{
			p.load(in);
			schedulerIP=p.getProperty("schedulerIP").trim();
			schedulerPort=Integer.parseInt(p.getProperty("schedulerPort").trim());

			cruisePort1=Integer.parseInt(p.getProperty("cruisePort1").trim());
			cruisePort2=Integer.parseInt(p.getProperty("cruisePort2").trim());
			jiankongPort=Integer.parseInt(p.getProperty("jiankongPort").trim());

			serverIP=p.getProperty("serverIP").trim();
			serverPort=Integer.parseInt(p.getProperty("serverPort").trim());

			localIP=p.getProperty("localIP").trim();
			localPort=Integer.parseInt(p.getProperty("localPort").trim());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setSchedulerIP(String schedulerIP){
		this.schedulerIP=schedulerIP;
	}

	public String getSchedulerIP(){
		return schedulerIP;
	}

	public void setSchedulerPort(int schedulerPort){
		this.schedulerPort=schedulerPort;
	}

	public int getSchedulerPort(){
		return schedulerPort;
	}

	public void setCruisePort1(int cruisePort1){
		this.cruisePort1=cruisePort1;
	}

	public int getCruisePort1(){
		return cruisePort1;
	}

	public void setCruisePort2(int cruisePort2){
		this.cruisePort2=cruisePort2;
	}

	public int getCruisePort2(){
		return cruisePort2;
	}

	public void setJiankongPort(int jiankongPort){
		this.jiankongPort=jiankongPort;
	}

	public int getJiankongPort(){
		return jiankongPort;
	}

	public void setServerIP(String serverIP){
		this.serverIP=serverIP;
	}

	public String getServerIP(){
		return serverIP;
	}

	public void setServerPort(int serverPort){
		this.serverPort=serverPort;
	}

	public int getServerPort(){
		return serverPort;
	}

	public void setLocalIP(String localIP){
		this.localIP=localIP;
	}

	public String getLocalIP(){
		return localIP;
	}

	public void setLocalPort(int localPort){
		this.localPort=localPort;
	}

	public int getLocalPort(){
		return localPort;
	}

	public static void main(String[] args){
		new SystemTool();
	}
}