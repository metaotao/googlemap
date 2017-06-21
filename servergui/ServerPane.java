package servergui;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tool.SystemTool;

/**
* @author tao
* @version 1.0
*/
public class ServerPane extends JPanel{

	private JLabel schedulerLabel;
	private JLabel scheduler;
	
	private JLabel linkLabel;
	private JLabel link;

	private JLabel localLabel;
	private JLabel local;

	private JLabel areaLabel;
	private JLabel area;
	
	public static String ipAddress=null;
	public static ServerPane serverPane;
	public static ServerPane instance(){
		if(serverPane==null){
			serverPane=new ServerPane();
		}
		return serverPane;
	}

	public ServerPane(){
		serverPane=this;
		setLayout(null);
		init();
	}

	public void init(){
		schedulerLabel=new JLabel("调度与监控IP：");
		schedulerLabel.setOpaque(true);
		schedulerLabel.setBackground(Color.YELLOW);
		setLabelColor(schedulerLabel);
		schedulerLabel.setBounds(30,30,150,30);
		add(schedulerLabel);

		String ip=SystemTool.instance().getSchedulerIP();
		
		scheduler=new JLabel(ip);
		scheduler.setOpaque(true);
		scheduler.setBackground(Color.YELLOW);
		setLabelColor(scheduler);
		scheduler.setBounds(180,30,150,30);
		add(scheduler);

		linkLabel=new JLabel("是否连接：");
		linkLabel.setOpaque(true);
		linkLabel.setBackground(Color.YELLOW);
		setLabelColor(linkLabel);
		linkLabel.setBounds(400,30,150,30);
		add(linkLabel);
		
		link=new JLabel("否");
		link.setOpaque(true);
		link.setBackground(Color.YELLOW);
		setLabelColor(link);
		link.setBounds(550,30,150,30);
		add(link);	

		localLabel=new JLabel("本机IP：");
		localLabel.setOpaque(true);
		localLabel.setBackground(Color.YELLOW);
		setLabelColor(localLabel);
		localLabel.setBounds(30,75,150,30);
		add(localLabel);
		
		try{
			ipAddress=InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception e){
		}
		local=new JLabel(ipAddress);
		local.setOpaque(true);
		local.setBackground(Color.YELLOW);
		setLabelColor(local);
		local.setBounds(180,75,150,30);
		add(local);	
		
		areaLabel=new JLabel("下载地区：");
		areaLabel.setOpaque(true);
		areaLabel.setBackground(Color.YELLOW);
		setLabelColor(areaLabel);
		areaLabel.setBounds(400,75,150,30);
		add(areaLabel);
		
		area=new JLabel();
		area.setOpaque(true);
		area.setBackground(Color.YELLOW);
		setLabelColor(area);
		area.setBounds(550,75,150,30);
		add(area);	
	}

	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setText1(String str){
		link.setText(str);
	}

	public void setText2(String str){
		area.setText(str);
	}

	public JLabel getLink(){
		return link;
	}
}