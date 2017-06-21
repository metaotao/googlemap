package clientgui;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

/**
* @author tao
* @version 1.0
*/



public class NumPane extends JPanel{
	private JLabel task_num;
	private JLabel task;
	
	private JLabel areaName;
	private JLabel area;

	private static NumPane numPane;
	public static NumPane instance(){
		if(numPane==null){
			numPane=new NumPane();
		}
		return numPane;
	}

	public NumPane(){
		numPane=this;
		setLayout(null);
		addComponent();
	}
	public void addComponent(){
		task_num=new JLabel("当前任务个数：");
		task_num.setOpaque(true);
		task_num.setBackground(Color.YELLOW);
		setLabelColor(task_num);
		task_num.setBounds(30,40,150,30);
		add(task_num);

		task=new JLabel();
		setLabelColor(task);
		task.setForeground(Color.RED);
		task.setBounds(200,40,150,30);
		add(task);

		areaName=new JLabel("当前地区：");
		areaName.setOpaque(true);
		areaName.setBackground(Color.YELLOW);
		setLabelColor(areaName);
		areaName.setBounds(400,40,150,30);
		add(areaName);

		area=new JLabel();
		setLabelColor(area);
		area.setForeground(Color.RED);
		area.setBounds(570,40,200,30);
		add(area);

	}
	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setAreaText(String str){
		area.setText(str);
	}

	public void setTaskText(String info){
	//	String str=new ProLatLngCal().calculate(info);
		task.setText(info);
		//incomplete.setText(str);
	}
}