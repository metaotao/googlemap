package servergui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tool.ProLatLngCal;

/**
* @author tao
* @version 1.0
*/

public class NumPane extends JPanel{

	private JLabel IPNumLabel;
	private JLabel IPNum;

	private JLabel taskIDLabel;
	private JLabel taskID;

	private JLabel taskNumLabel;
	private JLabel taskNum;

	private JLabel incompleteLabel;
	private JLabel incomplete;

	private static NumPane numPane;
	public static NumPane instance(){
		if(numPane==null){
			numPane=new NumPane();
		}
		return numPane;
	}

	public NumPane(){
		numPane=this;
		init();
	}

	public void init(){
		IPNumLabel=new JLabel("子蜘蛛数量：");
		IPNumLabel.setBounds(30,30,150,30);
		IPNumLabel.setOpaque(true);
		IPNumLabel.setBackground(Color.YELLOW);
		setLabelColor(IPNumLabel);
		add(IPNumLabel);

		IPNum=new JLabel();
		IPNum.setBounds(180,30,150,30);
		IPNum.setOpaque(true);
		IPNum.setBackground(Color.YELLOW);
		setLabelColor(IPNum);
		add(IPNum);

		taskIDLabel=new JLabel("接受任务标识：");
		taskIDLabel.setBounds(400,30,150,30);
		taskIDLabel.setOpaque(true);
		taskIDLabel.setBackground(Color.YELLOW);
		setLabelColor(taskIDLabel);
		add(taskIDLabel);

		taskID=new JLabel();
		taskID.setBounds(550,30,150,30);
		taskID.setOpaque(true);
		taskID.setBackground(Color.YELLOW);
		setLabelColor(taskID);
		add(taskID);

		taskNumLabel=new JLabel("任务数量：");
		taskNumLabel.setBounds(30,75,150,30);
		taskNumLabel.setOpaque(true);
		taskNumLabel.setBackground(Color.YELLOW);
		setLabelColor(taskNumLabel);
		add(taskNumLabel);
				
		taskNum=new JLabel();
		taskNum.setBounds(180,75,150,30);
		taskNum.setOpaque(true);
		taskNum.setBackground(Color.YELLOW);
		setLabelColor(taskNum);
		add(taskNum);

		incompleteLabel=new JLabel("未完成任务数量：");
		incompleteLabel.setBounds(400,75,150,30);
		incompleteLabel.setOpaque(true);
		incompleteLabel.setBackground(Color.YELLOW);
		setLabelColor(incompleteLabel);
		add(incompleteLabel);

		incomplete=new JLabel();
		incomplete.setBounds(550,75,150,30);
		incomplete.setOpaque(true);
		incomplete.setBackground(Color.YELLOW);
		setLabelColor(incomplete);
		add(incomplete);
	}
	
	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setTaskNum(ArrayList<String> list){
		String str=new ProLatLngCal().calculate(list);
		taskNum.setText(str);
		incomplete.setText(str);
	}

	public void setText(String str){
		IPNum.setText(str);
	}

	public void setTaskIDText(String str){
		taskID.setText(str);
	}


}