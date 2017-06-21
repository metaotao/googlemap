package scheduler;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
* @author tao
* @version 1.0
*/

public class MonitorPane extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CenterPane centerPane;
	private EastPane eastPane;
	
	private static MonitorPane monitorPane;
	public static MonitorPane instance(){
		if(monitorPane==null){
			monitorPane=new MonitorPane();
		}
		return monitorPane;
	}

	public MonitorPane(){
		monitorPane=this;
		setLayout(new BorderLayout());
		init();
	}

	public void init(){
		centerPane=new CenterPane();
		add(centerPane,BorderLayout.CENTER);

		eastPane=new EastPane();
		add(eastPane,BorderLayout.EAST);
	}

}