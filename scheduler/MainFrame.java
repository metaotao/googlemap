package scheduler;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
* @author tao
* @version 1.0
*/
public class MainFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JMenu menu1;
	private JMenuItem addItem1;
	private JMenuItem addItem2;
	
	private JPanel northPane;
	private JTabbedPane tabbedPane;
	private MonitorPane monitorPane;
	private ShowTaskPane showTaskPane;
	
	private JLabel titleLabel;

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
		setSize(1024,784);
		init();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
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

		titleLabel=new JLabel("负载均衡与调度 ");
		titleLabel.setFont(new Font("微软雅黑",Font.PLAIN,30));
		northPane.add(titleLabel);

		tabbedPane=new JTabbedPane();
		add(tabbedPane,BorderLayout.CENTER);

		showTaskPane=new ShowTaskPane();
		monitorPane=new MonitorPane();

		tabbedPane.add("任务显示",showTaskPane);
		tabbedPane.add("各蜘蛛监控与显示",monitorPane);
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