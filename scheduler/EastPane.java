package scheduler;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
/**
* @author tao
* @version 1.0
*/

public class EastPane extends JPanel{

	private static EastPane eastPane;
	private JLabel infoLabel;

	private JLabel numLabel;
	private JLabel num;

	private JLabel completeTaskLabel;
	private JLabel completeTask;

	private JLabel deadLabel;
	private JLabel dead;

	private JPanel northPane;
	private JPanel contentPane;

	public static EastPane instance(){
		if(eastPane==null){
			eastPane=new EastPane();
		}
		return eastPane;
	}

	public EastPane(){
		eastPane=this;
		setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(300,500));
		init();
	}

	public void init(){
		
		northPane=new JPanel();
		northPane.setPreferredSize(new Dimension(300,30));
		add(northPane,BorderLayout.NORTH);

		infoLabel=new JLabel("蜘蛛信息统计：");
		infoLabel.setForeground(Color.BLACK);
		infoLabel.setFont(new Font("微软雅黑",Font.PLAIN,16));
		//infoLabel.setBounds(10,10,150,30);
		northPane.add(infoLabel);

		contentPane=new JPanel();
		contentPane.setLayout(null);
		add(contentPane,BorderLayout.CENTER);

		numLabel=new JLabel("母蜘蛛数量：");
		setLabelColor(numLabel);
		numLabel.setBounds(10,20,200,30);
		contentPane.add(numLabel);

		num=new JLabel();
		setLabelColor(num);
		num.setBounds(210,20,80,30);
		contentPane.add(num);

		completeTaskLabel=new JLabel("完成任务母蜘蛛数量：");
		setLabelColor(completeTaskLabel);
		completeTaskLabel.setBounds(10,70,200,30);
		contentPane.add(completeTaskLabel);

		completeTask=new JLabel();
		setLabelColor(completeTask);
		completeTask.setBounds(210,70,80,30);
		contentPane.add(completeTask);

		deadLabel=new JLabel("死亡母蜘蛛数量：");
		setLabelColor(deadLabel);
		deadLabel.setBounds(10,120,200,30);
		contentPane.add(deadLabel);

		dead=new JLabel();
		setLabelColor(dead);
		dead.setBounds(210,120,80,30);
		contentPane.add(dead);
	}

	public void setLabelColor(JLabel label){
		label.setOpaque(true);
		label.setBackground(Color.YELLOW);
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setNumText(String str){
		num.setText(str);
	}
	
	public void setCompleteTaskText(String str){
		completeTask.setText(str);
	}

	public void setDeadText(String str){
		dead.setText(str);
	}

}