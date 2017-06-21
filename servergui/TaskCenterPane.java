package servergui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
* @author tao
* @version 1.0
*/

public class TaskCenterPane extends JPanel{
	private IPTreePane ipPane;
	private ShowTaskPane showTaskPane;

	private static TaskCenterPane taskPane;
	public static TaskCenterPane instance(){
		if(taskPane==null){
			taskPane=new TaskCenterPane();
		}
		return taskPane;
	}

	public TaskCenterPane(){
		taskPane=this;
		setLayout(new BorderLayout());		
	}
	public void add(JScrollPane pane){
		add(pane,BorderLayout.WEST);
	}
	/*public void addPane(String str){
		showTaskPane=new ShowTaskPane(str);
		add(showTaskPane,BorderLayout.CENTER);
	}*/

	public void addPane(JPanel pane){
		//showTaskPane=new ShowTaskPane(str);
		add(pane,BorderLayout.CENTER);
	}

	public void removePane(){
		remove(showTaskPane);
	}

}