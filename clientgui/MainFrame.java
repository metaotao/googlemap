package clientgui;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import tool.CountyLatLngCal;
import tool.InfoTool;
import rmi.RMIServerClient;
import rmi.HostService;
import bean.InfoBean;
import bean.MapClientBean;
import downloadmap.MonitorDownload;
import chart.RealTimeChange;

/**
* @author tao
* @version 1.0
*/
public class MainFrame extends JFrame{
	
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenu menu2;
	private JMenuItem addItem1;
	private JMenuItem addItem2;
	private JLabel titleLabel;

	private JPanel northPane;
	private JPanel centerPane;
	private JPanel southPane;
	private ServerPane serverPane;
	private NumPane numPane;
	private ShowPane showPane;
	private RealTimeChange realTimeChange;

	private String serverIP;
	
	//private String
	private double start_longitude;
	private double start_latitude;
	private double end_longitude;
	private double end_latitude;
	private int colRange;
	private int rowRange;
	private int sum;
	private int allSum;

	private RMIServerClient rsc;
	private InfoBean infoBean=new InfoBean();
	private MapClientBean clientBean=new MapClientBean();

	private HostService host;
	private ArrayList<String> sqlList;

	private static MainFrame f;
	public static MainFrame instance(){
		if(f==null){
			f=new MainFrame();
		}
		return f;
	}

	public MainFrame(){
		f=this;
		setLayout(new BorderLayout());
		setTitle("大规模图像下载与监控-服务器管理系统");
		setSize(1024,800);
		init();
		listener();
		startRMI();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("close");
			}
		});
		new ClientSender();
	}

	public void init(){
		menuBar=new JMenuBar();
		setJMenuBar(menuBar);
		menu1=new JMenu("说明");
		menuBar.add(menu1);

		addItem1=new JMenuItem("任务说明");
		menu1.add(addItem1);

		addItem2=new JMenuItem("基本说明");
		menu1.add(addItem2);
		addPane();
	}

	public void addPane(){
		northPane=new JPanel();
		northPane.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
		add(northPane,BorderLayout.NORTH);

		titleLabel=new JLabel("子蜘蛛");
		titleLabel.setFont(new Font("微软雅黑",Font.PLAIN,30));
		northPane.add(titleLabel);

		centerPane=new JPanel();
		centerPane.setLayout(null);
		add(centerPane,BorderLayout.CENTER);

		serverPane=new ServerPane();
		serverPane.setBounds(10,10,1000,120);
		serverPane.setBorder(new TitledBorder(null,"服务信息",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("微软雅黑",Font.PLAIN,16),Color.red));
		centerPane.add(serverPane);

		numPane=new NumPane();		
		numPane.setBounds(10,135,1000,100);
		numPane.setBorder(new TitledBorder(null,"数量信息",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("微软雅黑",Font.PLAIN,16),Color.red));
		centerPane.add(numPane);

		showPane=new ShowPane();
		showPane.setBounds(10,240,1000,200);
		showPane.setBorder(new TitledBorder(null,"执行情况",TitledBorder.DEFAULT_JUSTIFICATION,
			TitledBorder.DEFAULT_POSITION,new Font("微软雅黑",Font.PLAIN,16),Color.red));
		centerPane.add(showPane);

		realTimeChange=new RealTimeChange();
		southPane=realTimeChange.createChartPanel();
		southPane.setPreferredSize(new Dimension(950,240));
		add(southPane,BorderLayout.SOUTH);
	}

	public void setAllSum(int allSum){
		this.allSum=allSum;
	}

	public int getAllSum(){
		return allSum;
	}

	public void setRealTimeChange(RealTimeChange realTimeChange){
		this.realTimeChange=realTimeChange;
	}

	public RealTimeChange getRealTimeChange(){
		return realTimeChange;
	}

	public void startRMI(){
		try{
			rsc=new RMIServerClient(this);
			host=rsc.createServer1();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//远程对象调用
	public void onConnected(String info){
		try{
			System.out.println(info);
			serverPane.setText("是");
			
			new Thread(new HeatbeatSendThread()).start();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//远程对象调用
	public void getCountyInfo(String info){
		String[] split=info.split(" ");
		numPane.setAreaText(split[0]+" "+split[1]+" "+split[2]);
		start_longitude=Double.parseDouble(split[3]);
		start_latitude=Double.parseDouble(split[4]);
		end_longitude=Double.parseDouble(split[5]);
		end_latitude=Double.parseDouble(split[6]);
		
		clientBean.setProvince(split[0]);
		clientBean.setCity(split[1]);
		clientBean.setCounty(split[2]);

		clientBean.setStart_longitude(start_longitude);
		clientBean.setStart_latitude(start_latitude);
		clientBean.setEnd_longitude(end_longitude);
		clientBean.setEnd_latitude(end_latitude);
		
		System.out.println(info);

		String str=CountyLatLngCal.calculate(start_longitude,start_latitude,end_longitude,end_latitude);
		String[] s=str.split("x");
		colRange=Integer.parseInt(s[0]);
		rowRange=Integer.parseInt(s[1]);

		clientBean.setColRange(colRange);
		clientBean.setRowRange(rowRange);
		
		sum=colRange*rowRange;
		InfoTool.taskNum=sum;

		numPane.setTaskText(sum+"");
		showPane.setTaskText(sum+"");

		allSum+=sum;
		new Thread(new MonitorDownload()).start();
		
	}

	public void getTaskNum(int num){
		InfoTool.completeTask=num;
		System.out.println("任务标识为："+num);
		showPane.setTaskIDText(num+"");
	}

	public void setClientBean(MapClientBean clientBean){
		this.clientBean=clientBean;
	}

	public MapClientBean getClientBean(){
		return clientBean;
	}

	public void listener(){
		addItem2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				new BaseDialog();
			}
		});
	}	

	public static void main(String[] args){
		try{
			UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			new MainFrame();
		} 
		catch(Exception e){
		}
	}
}