package clientgui;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import tool.*;
/**
* @author tao
* @version 1.0
*/

public class ServerPane extends JPanel{

	private JLabel host_IPAddress;
	private JLabel hostIP_label;
	private JLabel server_IPAddress;
	private JLabel serverIP_label;
	private JLabel control_IPAddress;
	private JLabel controlIP_label;	
	private JLabel connection;
	private JLabel connection_label;

	private String schedulerIP;
	private String serverIP;

	private static ServerPane serverPane;
	public static ServerPane instance(){
		if(serverPane==null){
			serverPane=new ServerPane();
		}
		return serverPane;
	}

	public ServerPane(){
		serverPane=this;
		setLayout(null);
		addComponent();
	}

	public void addComponent(){
		host_IPAddress=new JLabel("本机IP：");
		host_IPAddress.setOpaque(true);
		host_IPAddress.setBackground(Color.YELLOW);
		host_IPAddress.setBounds(30,30,150,30);
		setLabelColor(host_IPAddress);
		add(host_IPAddress);
		
		try{
			String ip=InetAddress.getLocalHost().getHostAddress();
			hostIP_label=new JLabel(ip);
			setLabelColor(hostIP_label);
			hostIP_label.setBounds(200,30,150,30);
			hostIP_label.setForeground(Color.RED);			
			add(hostIP_label);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		serverIP=SystemTool.instance().getServerIP();

		server_IPAddress=new JLabel("服务端IP：");
		server_IPAddress.setOpaque(true);
		server_IPAddress.setBackground(Color.YELLOW);
		server_IPAddress.setBounds(400,30,150,30);
		setLabelColor(server_IPAddress);
		add(server_IPAddress);

		serverIP_label=new JLabel(serverIP);
		setLabelColor(serverIP_label);
		serverIP_label.setForeground(Color.RED);
		serverIP_label.setBounds(570,30,150,30);
		add(serverIP_label);
		
		schedulerIP=SystemTool.instance().getSchedulerIP();

		control_IPAddress=new JLabel("调度与监控IP：");
		control_IPAddress.setOpaque(true);
		control_IPAddress.setBackground(Color.YELLOW);
		control_IPAddress.setBounds(30,65,150,30);
		setLabelColor(control_IPAddress);
		add(control_IPAddress);

		controlIP_label=new JLabel(schedulerIP);
		setLabelColor(controlIP_label);
		controlIP_label.setForeground(Color.RED);
		controlIP_label.setBounds(200,65,150,30);
		add(controlIP_label);
		
		connection=new JLabel("是否连接：");
		connection.setOpaque(true);
		connection.setBackground(Color.YELLOW);
		connection.setBounds(400,65,150,30);
		setLabelColor(connection);
		add(connection);

		connection_label=new JLabel("否");
		setLabelColor(connection_label);
		connection_label.setForeground(Color.RED);
		connection_label.setBounds(570,65,100,30);
		
		add(connection_label);

	}

	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setText(String str){
		connection_label.setText(str);
	}

	public JLabel getConnection_label(){
		return connection_label;
	}


}