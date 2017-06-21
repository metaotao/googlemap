package servergui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import bean.InfoBean;
import rmi.HostService;
import rmi.RMIServerClient;
import tool.SystemTool;


public class IPDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel IPLabel;
	private static String str;
	private JTextField[][] fields;
	private JButton ensure;
	private JButton cancel;
	private Map<String,HostService> map=new HashMap<String,HostService>();
	private Map<String,String> map1=new HashMap<String,String>();
	private Map<String,Integer> map2=new HashMap<String,Integer>();
	private Map<Integer,String> map3=new HashMap<Integer,String>();	
	private Map<String,ShowTaskPane> map4=new HashMap<String,ShowTaskPane>();
	private Map<String,HeatbeatReceiveThread> map5=new HashMap<String,HeatbeatReceiveThread>();
	
	private ArrayList<String> disconnectList=new ArrayList<String>();
	private ArrayList<String> list=new ArrayList<String>();

	private RMIServerClient rsc;
	private HostService host;
	private ServerSocket serverSocket;
	
	private InfoBean[][] infoBean;
	private int x;
	private int y;
	private int IPSum;
	private HeatbeatReceiveThread thread;
	private int port=SystemTool.instance().getCruisePort2();

	private static IPDialog ipDialog;
	public static IPDialog instance(){
		if(ipDialog==null){
			ipDialog=new IPDialog(str);
		}
		return ipDialog;
	}

	public IPDialog(String str){
		this.str=str;
		
		ipDialog=this;
		setLocation(700,300);
		setLayout(null);
		init();
		listener();
			
		setVisible(true);
	}

	public void init(){
		IPLabel=new JLabel("请输入IP地址:");
		IPLabel.setBounds(10,10,150,30);
		IPLabel.setFont(new Font("幼圆",Font.PLAIN,16));
		add(IPLabel);
			
		try{
			String[] split=str.split("x");			
			x=Integer.parseInt(split[0]);
			y=Integer.parseInt(split[1]);
			fields=new JTextField[x][y];
			infoBean=new InfoBean[x][y];
			
			for(int i=0;i<fields.length;i++){
				for(int j=0;j<fields[i].length;j++){
					fields[i][j]=new JTextField();
					fields[i][j].setBounds(50+120*j,50+30*i,120,30);
					add(fields[i][j]);
				}
					
			}
		}
		catch(Exception e){
		}
			
		setSize(120*y+120,30*x+140);

		ensure=new JButton("连接完成");
		ensure.setFont(new Font("幼圆",Font.PLAIN,16));
		ensure.setBounds(60*(y-1),30*x+60,100,30);
		add(ensure);

		cancel=new JButton("放弃连接");
		cancel.setFont(new Font("幼圆",Font.PLAIN,16));
		cancel.setBounds(60*(y-1)+110,30*x+60,100,30);
		add(cancel);
			
	}

	public File createLog(String ipName){		
		File log=new File("log\\"+ipName+".log");
		try{
			if(!log.exists()){
				log.createNewFile();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return log;
	}
		
	public void getIPAddress(){
		try{
			serverSocket=new ServerSocket(port,5);	
			for(int i=0;i<fields.length;i++){
				for(int j=0;j<fields[i].length;j++){
					infoBean[i][j]=new InfoBean();

					String ip=fields[i][j].getText();
					if(!ip.equals("")){
						try{
							
							rsc=new RMIServerClient(this);
							host=rsc.createClient(ip);
							infoBean[i][j].setHost(host);
							infoBean[i][j].setIP(ip);
							IPSum+=1;
							infoBean[i][j].setID(IPSum);
							System.out.println("远程对象的IP为："+ip);
							host.onConnected("connected");

							String county=LeftCenterPane.instance().getOneCountyLatLng();
							host.getCountyInfo(county);
						//	createLog(ip);
							
							host.getTaskNum(LeftCenterPane.instance().getOneTaskNum());
							thread=new HeatbeatReceiveThread(serverSocket,ip,IPSum);
							new Thread(thread).start();

							map.put(ip,host);
							map1.put(ip,county);
							map2.put(ip,IPSum); 
							map3.put(IPSum,county);
							map5.put(ip,thread);
							list.add(ServerPane.ipAddress+" "+ip);
							ShowTaskPane showTaskPane=new ShowTaskPane(ip);
							map4.put(ip,showTaskPane);

														
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void listener(){
		ensure.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){				
				getIPAddress();				
				NumPane.instance().setText(IPSum+"");
				AddDialog.instance().setVisible(false);
				setVisible(false);
				TaskCenterPane.instance().add(new JScrollPane(new IPTreePane()));
				
				TaskCenterPane.instance().updateUI();
				TaskCenterPane.instance().repaint();
				new SendClientIP();

			}
		});			
			
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
	}

	public void setX(int x){
		this.x=x;
	}

	public int getX(){
		return x;
	}

	public void getY(int y){
		this.y=y;
	}

	public int getY(){
		return y;
	}

	public void setServerSocket(ServerSocket serverSocket){
		this.serverSocket=serverSocket;
	}

	public ServerSocket getServerSocket(){
		return serverSocket;
	}

	public void setThread(HeatbeatReceiveThread thread){
		this.thread=thread;
	}

	public HeatbeatReceiveThread getThread(){
		return thread;
	}

	public void setInfoBean(InfoBean[][] infoBean){
		this.infoBean=infoBean;
	}

	public InfoBean[][] getInfoBean(){
		return infoBean;
	}

	public void setMap(Map<String,HostService> map){
		this.map=map;
	}

	public Map<String,HostService> getMap(){
		return map;
	}

	public void setList(ArrayList<String> list){
		this.list=list;
	}

	public ArrayList<String> getList(){
		return list;
	}

	public void setMap1(Map<String,String> map1){
		this.map1=map1;
	}

	public Map<String,String> getMap1(){
		return map1;
	}

	public void setMap2(Map<String,Integer> map2){
		this.map2=map2;
	}

	public Map<String,Integer> getMap2(){
		return map2;
	}

	public void setMap3(Map<Integer,String> map3){
		this.map3=map3;
	}

	public Map<Integer,String> getMap3(){
		return map3;
	}
	
	public void setMap4(Map<String,ShowTaskPane> map4){
		this.map4=map4;
	}

	public Map<String,ShowTaskPane> getMap4(){
		return map4;
	}

	public void setMap5(Map<String,HeatbeatReceiveThread> map5){
		this.map5=map5;
	}

	public Map<String,HeatbeatReceiveThread> getMap5(){
		return map5;
	}

	public void setDisconnectList(ArrayList<String> disconnectList){
		this.disconnectList=disconnectList;
	}

	public ArrayList<String> getDisconnectList(){
		return disconnectList;
	}

	public void setIPSum(int IPSum){
		this.IPSum=IPSum;
	}

	public int getIPSum(){
		return IPSum;
	}
}
