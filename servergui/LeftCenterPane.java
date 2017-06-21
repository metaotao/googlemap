package servergui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import rmi.HostService;
import rmi.RMISchedulerServer;
import tool.InfoTool;


/**
* @author tao
* @version 1.0
*/

public class LeftCenterPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerPane serverPane;
	private TaskCenterPane centerPane;
	private NumPane numPane;

	private JButton startLink;
	private JButton sendInfo;

	private RMISchedulerServer rsc;
	public static LeftCenterPane leftCenterPane;
	private int listSum;

	private ArrayList<String> countyList;
	private ArrayList<Integer> taskList=new ArrayList<Integer>();
	private Map<String,HostService> hostMap;
	private Map<String,HeatbeatReceiveThread> threadMap;
	private HeatbeatReceiveThread thread;

	public static LeftCenterPane instance(){
		if(leftCenterPane==null){
			leftCenterPane=new LeftCenterPane();
		}
		return leftCenterPane;
	}
	public LeftCenterPane(){
		leftCenterPane=this;
		setLayout(null);
		init();
		startRMI();
		listener();

	}

	public void init(){
		serverPane=new ServerPane(); 
		serverPane.setBounds(20,10,900,120);
		add(serverPane);
		
		serverPane.setBorder(new TitledBorder(null,"������Ϣ",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("΢���ź�",Font.PLAIN,16),Color.red));
		
		numPane=new NumPane();
		numPane.setBounds(20,150,900,130);
		numPane.setLayout(null);
		numPane.setBorder(new TitledBorder(null,"������������Ϣ",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("΢���ź�",Font.PLAIN,16),Color.red));
		add(numPane);

		centerPane=new TaskCenterPane();
		centerPane.setLayout(new BorderLayout());
		centerPane.setBounds(20,300,900,310);
		add(centerPane);
		
		centerPane.setBorder(new TitledBorder(null,"���������Ϣ",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("΢���ź�",Font.PLAIN,16),Color.red));
		
		startLink=new JButton("��ʼ����");
		startLink.setBounds(450,630,150,40);
		startLink.setFont(new Font("��Բ",Font.PLAIN,16));
		add(startLink);

		sendInfo=new JButton("��������");
		sendInfo.setBounds(650,630,150,40);
		sendInfo.setFont(new Font("��Բ",Font.PLAIN,16));
		add(sendInfo);
	}

	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("΢���ź�",Font.PLAIN,16));
	}

	public void startRMI(){
		try{
			rsc=new RMISchedulerServer(this);
			rsc.createServer1();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//Զ�̶������
	public void onConnected(String info){
		try{
			System.out.println(info);
			serverPane.setText1("��");
			new Thread(new HeatbeatSendThread()).start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Զ�̶������
	public void getDownloadInfo(String info){
		try{
			serverPane.setText2(info);
			System.out.println("��ǰ���ص���Ϊ��"+info);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Զ�̶������
	public void getProLatLng(ArrayList<String> list){
		NumPane.instance().setTaskNum(list);
		System.out.println(list);
		
	}
	
	//Զ�̶������
	public void getCountyLatLng(ArrayList<String> list){
		countyList=list;
		listSum=list.size();
		System.out.println("��������������Ϊ��"+listSum);
		for(int i=0;i<listSum;i++){
			taskList.add(i+1);
		}
		System.out.println(list);
	}
	
	public int getOneTaskNum(){
		return taskList.remove(0);
	}

	public boolean taskIsEmpty(){
		if(taskList.size()==0){
			return false;
		}
		return true;
	}

		//��ȡ������γ����Ϣ
	public String getOneCountyLatLng(){
		return countyList.remove(0);
	}

	public boolean isEmpty(){
		if(countyList.size()==0){
			return false;
		}
		return true;
	}

	public void getTaskNum(int num){
		InfoTool.serverCompleteTask=num;
		System.out.println("��ǰ�����ʶΪ��"+num);
		numPane.setTaskIDText(num+"");
		if(num>1){
			hostMap=IPDialog.instance().getMap();
			threadMap=IPDialog.instance().getMap5();

			try{
				for(String key:hostMap.keySet()){			
					HostService host=hostMap.get(key);
						
					String county=getOneCountyLatLng();
					host.getCountyInfo(county);						
					host.getTaskNum(getOneTaskNum());
					thread=threadMap.get(key);
					new Thread(thread).start();
				}				
			}	
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void listener(){
		startLink.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new AddDialog();
			}
		});
	}

	public void setListSum(int listSum){
		this.listSum=listSum;
	}

	public int getListSum(){
		return listSum;
	}
}