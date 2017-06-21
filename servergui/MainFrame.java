package servergui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


/**
* @author tao
*/
public class MainFrame extends JFrame{
	
	private JLabel titleLabel;
	private JPanel northPane;
	private LeftCenterPane leftCenterPane;

	public MainFrame(){
		setLayout(new BorderLayout());
		setTitle("大规模图像下载与监控-服务器管理系统");
		setSize(1024,784);
		addPane();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void addPane(){
		northPane=new JPanel();
		northPane.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
		
		add(northPane,BorderLayout.NORTH);
		addComponent();

		leftCenterPane=new LeftCenterPane();
		add(leftCenterPane,BorderLayout.CENTER);
	}

	public void addComponent(){
		titleLabel=new JLabel("母蜘蛛");
		titleLabel.setFont(new Font("微软雅黑",Font.PLAIN,30));
		northPane.add(titleLabel);
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