package scheduler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;

import java.util.Map;
import java.util.ArrayList;

/**
* @author tao
* @version 1.0
*/

public class CenterPane extends JPanel{

	private JPanel northPane;
	
	private JButton normalButton;
	private JButton deadButton;
	private JButton sleepButton;
	
	private JDialog normalDialog=new JDialog();
	private JDialog deadDialog=new JDialog();
	private JDialog sleepDialog=new JDialog();

	private JLabel label1;
	private JLabel normalIP1;
	private JLabel normalIP2;
	private JLabel normalIP3;
	private JLabel normalIP4;
	private JLabel normalIP5;
	private JLabel normalIP6;

	private JLabel label2;
	private JLabel deadIP1;
	private JLabel deadIP2;
	private JLabel deadIP3;

	private JLabel label3;
	private JLabel sleepIP1;
	private JLabel sleepIP2;
	private JLabel sleepIP3;

	private Map<Integer,String> map;		
	private ArrayList<String> disconnectList;
	private ContentPane contentPane;
	
	private ArrayList<String> list=null;
	private JLabel[] label;
	
	private static CenterPane centerPane;

	public static CenterPane instance(){
		if(centerPane==null){
			centerPane=new CenterPane();
		}
		return centerPane;
	}

	public CenterPane(){
		centerPane=this;
		setLayout(new BorderLayout());
		init();
		listener();
	}

	public void init(){
		JToolBar toolBar=new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.CENTER,20,5));
		add(toolBar,BorderLayout.NORTH);

		normalButton=new JButton("’˝≥£");
		normalButton.setBackground(new Color(210,240,240));
		normalButton.setFont(new Font("Œ¢»Ì—≈∫⁄",Font.PLAIN,15));
		toolBar.add(normalButton);

		deadButton=new JButton("À¿Õˆ");
		deadButton.setBackground(new Color(210,240,240));
		deadButton.setFont(new Font("Œ¢»Ì—≈∫⁄",Font.PLAIN,15));
		toolBar.add(deadButton);

		sleepButton=new JButton("–›√ﬂ");
		sleepButton.setBackground(new Color(210,240,240));
		sleepButton.setFont(new Font("Œ¢»Ì—≈∫⁄",Font.PLAIN,15));
		toolBar.add(sleepButton);
		
		//contentPane=new ContentPane();
		//add(contentPane,BorderLayout.CENTER);
	}

	public void addContentPane(ArrayList<String> list){
		contentPane=new ContentPane(list);
		add(contentPane,BorderLayout.CENTER);
	}
	
	public void listener(){
		//
		normalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				initCom();
				normalDialog.setSize(400,300);
				setLayout(null);
				normalDialog.setLocation(400,300);

				label1=new JLabel("’˝≥£‘À––÷©÷Î”–£∫");
				label1.setBounds(10,20,150,30);
				getLabel1(label1);
				normalDialog.add(label1);

				getNormalSpiderIP(normalDialog);
				normalDialog.setVisible(true);
			}
		});

		deadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				deadDialog.setSize(400,300);
				setLayout(null);
				deadDialog.setLocation(400,300);
				
				label2=new JLabel("À¿Õˆ÷©÷Î”–£∫");
				label2.setBounds(10,20,150,30);
				getLabel1(label2);
				deadDialog.add(label2);

				getDeadSpiderIP(deadDialog);
				deadDialog.setVisible(true);
			}
		});

		sleepButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				sleepDialog.setSize(400,300);
				setLayout(null);
				sleepDialog.setLocation(400,300);

				label3=new JLabel("–›√ﬂ÷©÷Î”–£∫");
				label3.setBounds(10,20,150,30);
				getLabel1(label3);
				sleepDialog.add(label2);

				sleepDialog.setVisible(true);

			}
		});
	}
	 
	public void getLabel1(JLabel label){
		label.setFont(new Font("Œ¢»Ì—≈∫⁄",Font.PLAIN,16));
		label.setForeground(Color.BLUE);
	}

	public void getLabel2(JLabel label){
		label.setFont(new Font("Œ¢»Ì—≈∫⁄",Font.PLAIN,16));
		label.setForeground(Color.RED);
	}

	public void initCom(){
		list=IPDialog.instance().getList();
		label=new JLabel[list.size()];
	}

	public void getNormalSpiderIP(JDialog dialog){
		
		for(int i=0;i<list.size();i++){
			label[i]=new JLabel(list.get(i));
			label[i].setBounds(40,40+30*i,150,30);
			dialog.add(label[i]);
		}
	}

	public void getDeadSpiderIP(JDialog dialog){
		disconnectList=IPDialog.instance().getThread().getDisconnectList();
		map=IPDialog.instance().getMap2();
		JLabel[] label=new JLabel[disconnectList.size()];
		for(int i=0;i<disconnectList.size();i++){
			label[i]=new JLabel(disconnectList.get(i));
			label[i].setBounds(40,40+30*i,150,30);
			dialog.add(label[i]);

			if(disconnectList.get(i).equals(map.get(i+1))){
				normalDialog.remove(label[i]);
			}
		}
	}
}