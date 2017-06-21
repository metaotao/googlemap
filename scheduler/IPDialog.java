package scheduler;
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
import javax.swing.JTextField;

import bean.InfoBean;
import rmi.RMISchedulerServer;
import rmi.SchedulerService;
import sql.GetProInfo;
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

	private RMISchedulerServer rsc;
	private SchedulerService scheduler;

	private InfoBean[][] infoBean;
	private int x;
	private int y;
	private int sum;
	private Map<String,SchedulerService> map=new HashMap<String,SchedulerService>();
	private Map<Integer,String> map2=new HashMap<Integer,String>();
	private Map<Integer,String> map3=new HashMap<Integer,String>();
	private ArrayList<String> list=new ArrayList<String>();
	private ServerSocket serverSocket;
	private HeatbeatReceiveThread thread;
	private int port=SystemTool.instance().getCruisePort1();

	private static IPDialog ipDialog;
	public static IPDialog instance(){
		if(ipDialog==null){
			ipDialog=new IPDialog(str);
		}
		return ipDialog;
	}

	public IPDialog(String str){
		IPDialog.str=str;		
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
							rsc=new RMISchedulerServer(this);
							scheduler=rsc.createClient(ip);
							infoBean[i][j].setScheduler(scheduler);
							infoBean[i][j].setIP(ip);
							System.out.println("远程对象的IP为："+ip);
							scheduler.onConnected("connected");	

							String pro=ShowTaskPane.instance().getOneProInfo();
							scheduler.getDownloadInfo(pro);
							scheduler.getProLatLng(GetProInfo.instance().getProLatLng(pro));
							scheduler.getCountyLatLng(GetProInfo.instance().getCountyLatLng(pro));
							//createLog(ip);
							sum+=1;
							
							scheduler.getTaskNum(ShowTaskPane.instance().getOneTaskNum());
							thread=new HeatbeatReceiveThread(serverSocket,ip,sum);
							new Thread(thread).start();
							
							map.put(ip,scheduler);
							map2.put(sum,ip);
							map3.put(sum, pro);
							list.add(ip);
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
				ShowTaskPane.instance().setText(sum+"");
				EastPane.instance().setNumText(sum+"");
				EastPane.instance().setCompleteTaskText("0");
				EastPane.instance().setDeadText("0");

				AddDialog.instance().setVisible(false);
				setVisible(false);
			}
		});			
			
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
	}

	public void setServerSocket(ServerSocket serverSocket){
		this.serverSocket=serverSocket;
	}

	public ServerSocket getServerSocket(){
		return serverSocket;
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

	public void setInfoBean(InfoBean[][] infoBean){
		this.infoBean=infoBean;
	}

	public InfoBean[][] getInfoBean(){
		return infoBean;
	}

	public void setMap(Map<String,SchedulerService> map){
		this.map=map;
	}

	public Map<String,SchedulerService> getMap(){
		return map;
	}

	public void setMap2(Map<Integer,String> map2){
		this.map2=map2;
	}

	public Map<Integer,String> getMap2(){
		return map2;
	}

	public Map<Integer, String> getMap3() {
		return map3;
	}

	public void setMap3(Map<Integer, String> map3) {
		this.map3 = map3;
	}

	public void setList(ArrayList<String> list){
		this.list=list;
	}

	public ArrayList<String> getList(){
		return list;
	}

	public void setSum(int sum){
		this.sum=sum;
	}

	public int getSum(){
		return sum;
	}

	public void setThread(HeatbeatReceiveThread thread){
		this.thread=thread;
	}

	public HeatbeatReceiveThread getThread(){
		return thread;
	}

	public ArrayList<String> getDisconnectList() {
		// TODO Auto-generated method stub
		return null;
	}

}
