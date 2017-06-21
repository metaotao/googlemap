package clientgui;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
/**
* @author tao
* @version 1.0
*/

public class ShowPane extends JPanel{
	private JLabel receive_num;
	private JLabel receive;
	private JLabel complete_num;
	private JLabel complete;
	private JLabel current_state;
	private JLabel current;
	private JLabel task_queue;
	private JLabel task;
	private JLabel execute_thread;
	private JLabel execute;
	private JLabel strong_thread;
	private JLabel strong;
	private JLabel time_label;
	private JLabel time;
	private JLabel sleep_label;
	private JLabel sleep;
	
	private static ShowPane showPane;
	public static ShowPane instance(){
		if(showPane==null){
			showPane=new ShowPane();
		}
		return showPane;
	}

	public ShowPane(){
		setLayout(null);
		showPane=this;
		addComponent();
	}

	public void addComponent(){
		receive_num=new JLabel("总共接受数量：");
		receive_num.setOpaque(true);
		receive_num.setBackground(Color.YELLOW);
		setLabelColor(receive_num);
		receive_num.setBounds(30,30,150,30);
		add(receive_num);

		receive=new JLabel();
		setLabelColor(receive);
		receive.setForeground(Color.RED);
		receive.setBounds(200,30,200,30);
		add(receive);

		complete_num=new JLabel("已完成数量：");
		complete_num.setOpaque(true);
		complete_num.setBackground(Color.YELLOW);
		setLabelColor(complete_num);
		complete_num.setBounds(400,30,150,30);
		add(complete_num);

		complete=new JLabel();
		setLabelColor(complete);
		complete.setForeground(Color.RED);
		complete.setBounds(570,30,150,30);
		add(complete);

		current_state=new JLabel("当前状态：");
		current_state.setOpaque(true);
		current_state.setBackground(Color.YELLOW);
		setLabelColor(current_state);
		current_state.setBounds(30,70,150,30);
		add(current_state);

		current=new JLabel();
		setLabelColor(current);
		current.setForeground(Color.RED);
		current.setBounds(200,70,200,30);
		add(current);

		task_queue=new JLabel("接受任务标识：");
		task_queue.setOpaque(true);
		task_queue.setBackground(Color.YELLOW);
		setLabelColor(task_queue);
		task_queue.setBounds(400,70,150,30);
		add(task_queue);

		task=new JLabel();
		setLabelColor(task);
		task.setForeground(Color.RED);
		task.setBounds(570,75,150,30);
		add(task);

		strong_thread=new JLabel("程序是否健壮：");
		strong_thread.setOpaque(true);
		strong_thread.setBackground(Color.YELLOW);
		setLabelColor(strong_thread);
		strong_thread.setBounds(30,110,150,30);
		add(strong_thread);

		strong=new JLabel("否");
		setLabelColor(strong);
		strong.setForeground(Color.RED);
		strong.setBounds(200,110,150,30);
		add(strong);
		
		execute_thread=new JLabel("当前执行线程数：");
		execute_thread.setOpaque(true);
		execute_thread.setBackground(Color.YELLOW);
		setLabelColor(execute_thread);
		execute_thread.setBounds(400,110,150,30);
		add(execute_thread);

		execute=new JLabel("0");
		setLabelColor(execute);
		execute.setForeground(Color.RED);
		execute.setBounds(570,110,150,30);
		add(execute);

		time_label=new JLabel("每次下载时间：");
		time_label.setOpaque(true);
		time_label.setBackground(Color.YELLOW);
		setLabelColor(time_label);
		time_label.setBounds(30,150,150,30);
		add(time_label);

		time=new JLabel("10 min");
		setLabelColor(time);
		time.setForeground(Color.RED);
		time.setBounds(200,150,100,30);
		add(time);

		sleep_label=new JLabel("是否休眠：");
		sleep_label.setOpaque(true);
		sleep_label.setBackground(Color.YELLOW);
		setLabelColor(sleep_label);
		sleep_label.setBounds(400,150,150,30);
		add(sleep_label);

		sleep=new JLabel();
		setLabelColor(sleep);
		sleep.setForeground(Color.RED);
		sleep.setBounds(570,150,100,30);
		add(sleep);
	}

	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setTaskText(String str){
		receive.setText(str);
	}

	public void setSleepText(String str){
		sleep.setText(str);
	}

	public void setCurrentText(String str){
		current.setText(str);
	}

	public void setExecuteText(String str){
		execute.setText(str);
	}

	public void setStrongText(String str){
		strong.setText(str);
	}

	public void setCompleteText(String str){
		complete.setText(str);
	}

	public void setTaskIDText(String str){
		task.setText(str);
	}

}