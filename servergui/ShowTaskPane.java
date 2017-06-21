package servergui;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
/**
* @author tao
* @version 1.0
*/

public class ShowTaskPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel zoom_label;
	private JLabel zoom;

	private JLabel link_label;
	private JLabel link;

	private JLabel taskIP;
	private JLabel taskIP_label;

	private JLabel num_label;
	private JLabel num;

	private JLabel area_label;
	private JLabel area;

	private JLabel breakarea_label;
	private JLabel breakarea;

	private JLabel status_label;
	private JLabel status;

	private static ShowTaskPane showTaskPane;
	private static String str;

	public static ShowTaskPane instance(){
		if(showTaskPane==null){
			showTaskPane=new ShowTaskPane(str);
		}
		return showTaskPane;
	}

	public ShowTaskPane(String str){
		this.str=str;
		showTaskPane=this;
		setLayout(null);
		init();
	}

	public void init(){

		zoom_label=new JLabel("下载等级：");
		zoom_label.setOpaque(true);
		zoom_label.setBackground(Color.YELLOW);
		setLabelColor(zoom_label);
		zoom_label.setBounds(30,30,150,30);
		add(zoom_label);

		zoom=new JLabel("   16");
		zoom.setOpaque(true);
		zoom.setBackground(Color.YELLOW);
		setLabelColor(zoom);
		zoom.setBounds(180,30,150,30);
		add(zoom);

		link_label=new JLabel("是否连接：");
		link_label.setOpaque(true);
		link_label.setBackground(Color.YELLOW);
		setLabelColor(link_label);
		link_label.setBounds(360,30,150,30);
		add(link_label);

		link=new JLabel("是");
		link.setOpaque(true);
		link.setBackground(Color.YELLOW);
		setLabelColor(link);
		link.setBounds(510,30,150,30);
		add(link);

		taskIP_label=new JLabel("任务IP：");
		taskIP_label.setOpaque(true);
		taskIP_label.setBackground(Color.YELLOW);
		setLabelColor(taskIP_label);
		taskIP_label.setBounds(30,80,150,30);
		add(taskIP_label);

		taskIP=new JLabel(str);
		taskIP.setOpaque(true);
		taskIP.setBackground(Color.YELLOW);
		setLabelColor(taskIP);
		taskIP.setBounds(180,80,150,30);
		add(taskIP);

		num_label=new JLabel("任务标识：");
		num_label.setOpaque(true);
		num_label.setBackground(Color.YELLOW);
		setLabelColor(num_label);
		num_label.setBounds(360,80,150,30);
		add(num_label);

		num=new JLabel();
		num.setOpaque(true);
		num.setBackground(Color.YELLOW);
		setLabelColor(num);
		num.setBounds(510,80,150,30);
		add(num);

		area_label=new JLabel("正在下载区域：");
		area_label.setOpaque(true);
		area_label.setBackground(Color.YELLOW);
		setLabelColor(area_label);
		area_label.setBounds(30,130,150,30);
		add(area_label);

		area=new JLabel();
		area.setOpaque(true);
		area.setBackground(Color.YELLOW);
		setLabelColor(area);
		area.setBounds(180,130,150,30);
		add(area);

		breakarea_label=new JLabel("断开时区域：");
		breakarea_label.setOpaque(true);
		breakarea_label.setBackground(Color.YELLOW);
		setLabelColor(breakarea_label);
		breakarea_label.setBounds(360,130,150,30);
		add(breakarea_label);

		breakarea=new JLabel("无");
		breakarea.setOpaque(true);
		breakarea.setBackground(Color.YELLOW);
		setLabelColor(breakarea);
		breakarea.setBounds(510,130,150,30);
		add(breakarea);

		status_label=new JLabel("当前状态：");
		status_label.setOpaque(true);
		status_label.setBackground(Color.YELLOW);
		setLabelColor(status_label);
		status_label.setBounds(30,180,150,30);
		add(status_label);

		status=new JLabel("正在下载");
		status.setOpaque(true);
		status.setBackground(Color.YELLOW);
		setLabelColor(status);
		status.setBounds(180,180,150,30);
		add(status);
	}

	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void setLinkText(String str){
		link.setText(str);
	}

	public void setStatusText(String str){
		status.setText(str);
	}

	public void setBreakAreaText(String str){
		breakarea.setText(str);
	}

	public void setNumText(String str){
		num.setText(str);
	}

	public void setAreaText(String str){
		area.setText(str);
	}

}