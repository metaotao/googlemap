package scheduler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JDialog;

import java.awt.BorderLayout;
import java.awt.Font;

public class ScreenDialog extends JDialog{
	private JPanel southPane;
	private ScreenPane screenPane;
	private JLabel ipLabel;

	private static ScreenDialog screenDialog;
	private static String ip;
	public static ScreenDialog instance(){
		if(screenDialog==null){
			screenDialog=new ScreenDialog(ip);
		}
		return screenDialog;
	}

	public ScreenDialog(String ip){
		this.ip=ip;
		screenDialog=this;
		setLocation(400,300);
		setSize(500,400);
		setLayout(new BorderLayout());

		init(ip);
		setVisible(true);
	}

	public void init(String ip){
		southPane=new JPanel();
		add(southPane,BorderLayout.SOUTH);

		ipLabel=new JLabel(ip);
		ipLabel.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,20));
		southPane.add(ipLabel);

		screenPane=new ScreenPane();
		add(screenPane,BorderLayout.CENTER);
	}

	public void setScreenPane(ScreenPane screenPane){
		this.screenPane=screenPane;
	}

	public ScreenPane getScreenPane(){
		return screenPane;
	}

}