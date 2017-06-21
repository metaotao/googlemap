package scheduler;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sql.GetProInfo;
import tool.NationLatLngCal;


public class ShowTaskPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel zoomLabel;
	private JLabel spiderLabel;
	private JLabel spider;
	private JLabel downloadSumLabel;
	private JLabel downloadSum;
	private JLabel taskSumLabel;
	private JLabel taskSum;
	
	private JLabel finishTaskLabel;
	private JLabel finishTask;
	private JLabel unfinishTaskLabel;
	private JLabel unfinishTask;
	private JLabel timeLabel;
	private JLabel time;

	private JButton startMonitor;
	private JButton endMonitor;
	
	private static ShowTaskPane showTaskPane;
	private ArrayList<String> list=GetProInfo.instance().executeQuery();
	private ArrayList<String> list1=GetProInfo.instance().executeQuery();
	private ArrayList<Integer> taskList=new ArrayList<Integer>();
	
	private int taskListSum;

	public static ShowTaskPane instance(){
		if(showTaskPane==null){
			showTaskPane=new ShowTaskPane();
		}
		return showTaskPane;
	}

	public ShowTaskPane(){
		showTaskPane=this;

		setLayout(null);
		init();
		listener();
		getTaskSum();
	}

	public void init(){
		zoomLabel=new JLabel("下载级数：           16");
		zoomLabel.setOpaque(true);
		zoomLabel.setBackground(Color.YELLOW);
		setLabelColor(zoomLabel);
		zoomLabel.setBounds(80,40,300,30);
		add(zoomLabel);

		spiderLabel=new JLabel("母蜘蛛数量：");
		spiderLabel.setOpaque(true);
		spiderLabel.setBackground(Color.YELLOW);
		setLabelColor(spiderLabel);
		spiderLabel.setBounds(80,100,150,30);
		add(spiderLabel);

		spider=new JLabel();
		spider.setOpaque(true);
		spider.setBackground(Color.YELLOW);
		setLabelColor(spider);
		spider.setBounds(230,100,150,30);
		add(spider);

		downloadSumLabel=new JLabel("剩余下载总数量：");
		downloadSumLabel.setOpaque(true);
		downloadSumLabel.setBackground(Color.YELLOW);
		setLabelColor(downloadSumLabel);
		downloadSumLabel.setBounds(80,160,150,30);
		add(downloadSumLabel);

		String amount=new NationLatLngCal().calculate();
		downloadSum=new JLabel(amount);
		downloadSum.setOpaque(true); 
		downloadSum.setBackground(Color.YELLOW);
		setLabelColor(downloadSum);
		downloadSum.setBounds(230,160,150,30);
		add(downloadSum);

		taskSumLabel=new JLabel("当前分发任务数量：");
		taskSumLabel.setOpaque(true);
		taskSumLabel.setBackground(Color.YELLOW);
		setLabelColor(taskSumLabel);
		taskSumLabel.setBounds(80,220,150,30);
		add(taskSumLabel);

		taskSum=new JLabel();
		taskSum.setOpaque(true);
		taskSum.setBackground(Color.YELLOW);
		setLabelColor(taskSum);
		taskSum.setBounds(230,220,150,30);
		add(taskSum);

		finishTaskLabel=new JLabel("已完成任务数量：");
		finishTaskLabel.setOpaque(true);
		finishTaskLabel.setBackground(Color.YELLOW);
		setLabelColor(finishTaskLabel);
		finishTaskLabel.setBounds(500,220,150,30);
		add(finishTaskLabel);

		finishTask=new JLabel();
		finishTask.setOpaque(true);
		finishTask.setBackground(Color.YELLOW);
		setLabelColor(finishTask);
		finishTask.setBounds(650,220,150,30);
		add(finishTask);

		unfinishTaskLabel=new JLabel("未完成任务数量：");
		unfinishTaskLabel.setOpaque(true);
		unfinishTaskLabel.setBackground(Color.YELLOW);
		setLabelColor(unfinishTaskLabel);
		unfinishTaskLabel.setBounds(80,280,150,30);
		add(unfinishTaskLabel);

		unfinishTask=new JLabel();
		unfinishTask.setOpaque(true);
		unfinishTask.setBackground(Color.YELLOW);
		setLabelColor(unfinishTask);
		unfinishTask.setBounds(230,280,150,30);
		add(unfinishTask);

		timeLabel=new JLabel("下载执行时间：");
		timeLabel.setOpaque(true);
		timeLabel.setBackground(Color.YELLOW);
		setLabelColor(timeLabel);
		timeLabel.setBounds(80,340,150,30);
		add(timeLabel);

		time=new JLabel("10 min");
		time.setOpaque(true);
		time.setBackground(Color.YELLOW);
		setLabelColor(time);
		time.setBounds(230,340,150,30);
		add(time);	

		startMonitor=new JButton("开始监控");
		startMonitor.setBounds(400,500,180,40);
		startMonitor.setFont(new Font("微软雅黑",Font.PLAIN,17));
		add(startMonitor);

		endMonitor=new JButton("结束监控");
		endMonitor.setBounds(620,500,180,40);
		endMonitor.setFont(new Font("微软雅黑",Font.PLAIN,17));
		add(endMonitor);
	}

	public void setText(String str){
		spider.setText(str);
		taskSum.setText(str);
		finishTask.setText("0");
		unfinishTask.setText(str);
	}


	public void setLabelColor(JLabel label){
		label.setForeground(Color.BLACK);
		label.setFont(new Font("微软雅黑",Font.PLAIN,16));
	}

	public void listener(){
		startMonitor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				startMonitor.setBackground(Color.WHITE);
				new AddDialog();
			}
		});

		endMonitor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				endMonitor.setBackground(new Color(214,217,223));
			}
		});
	}

	public void getTaskSum(){
		for(int i=0;i<list1.size();i++){
			taskList.add(i+1);
		}
		taskListSum=list1.size();
	}

	public int getOneTaskNum(){
		return taskList.remove(0);
	}

	public boolean taskIsEmpty(){
		if(taskList.size()==0){
			return false;
		}
		return true;
	}

	public String getOneProInfo(){
		return list.remove(0);
	}

	public boolean isEmpty(){
		if(list.size()==0){
			return false;
		}
		return true;
	}

	public int getTaskListSum() {
		return taskListSum;
	}

	public void setTaskListSum(int taskListSum) {
		this.taskListSum = taskListSum;
	}
	
	
}