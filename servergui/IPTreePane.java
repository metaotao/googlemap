package servergui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import bean.InfoBean;
import tool.SystemTool;

/**
* @author tao
* @version 1.0
*/

public class IPTreePane extends JPanel{
	private static IPTreePane ipTree;
	private InfoBean[][] infoBean;
	private String ip;
	private String serverIP;
	private Map<String,String> map=IPDialog.instance().getMap1();			
	private Map<String,Integer> map1=IPDialog.instance().getMap2();
	private Map<String,ShowTaskPane> map2=IPDialog.instance().getMap4();
	private Map<String,HeatbeatReceiveThread> map3=IPDialog.instance().getMap5();
	private HeatbeatReceiveThread thread;
	
	private int count;
	private String temp;

	public static IPTreePane instance(){
		if(ipTree==null){
			ipTree=new IPTreePane();
		}
		return ipTree;
	}

	public IPTreePane(){
		setLayout(new BorderLayout());
		init();
		
	}

	public void init(){
		infoBean=IPDialog.instance().getInfoBean();
		try{
			ip=InetAddress.getLocalHost().getHostAddress();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		JTree tree=createTree();
		listener(tree);
	}

	//创建IP节点树
	public JTree createTree(){
		serverIP=SystemTool.instance().getServerIP();
	//	DefaultMutableTreeNode node=new DefaultMutableTreeNode("192.168.24.79");
		DefaultMutableTreeNode node1=new DefaultMutableTreeNode(serverIP);
		//DefaultMutableTreeNode node2=new DefaultMutableTreeNode("192.168.24.79");
		//node.add(node1);

		DefaultMutableTreeNode root=new DefaultMutableTreeNode("IP节点树");
		root.add(node1);	
		
		for(int i=0;i<infoBean.length;i++){
			for(int j=0;j<infoBean[i].length;j++){
				DefaultMutableTreeNode node2=new DefaultMutableTreeNode(infoBean[i][j].getIP());
				node1.add(node2);
			}
		}
		JTree tree=new JTree(root);
		//tree.setRootVisible(false);
		tree.setRowHeight(25); 
		tree.setBackground(new Color(214,217,223));
		tree.setFont(new Font("微软雅黑",Font.PLAIN,16));
		TreePath treePath=new TreePath(root);
		expandTree(tree,root);
		
		add(tree,BorderLayout.WEST);
		updateUI();
		repaint();
		return tree;
	}

	public void expandTree(JTree tree,DefaultMutableTreeNode root){
		Enumeration<?> enumeration=root.preorderEnumeration();
		while(enumeration.hasMoreElements()){
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)enumeration.nextElement();
			if(!node.isLeaf()){
				TreePath path=new TreePath(node.getPath());
				tree.expandPath(path);
			}
		}
	}

	public void listener(JTree tree){
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e){
				String str=null;
				TreePath path=tree.getSelectionPath();
				if(path==null) return;
				DefaultMutableTreeNode node=(DefaultMutableTreeNode)path.getLastPathComponent();
				str=node.toString();

				ShowTaskPane.instance().updateUI();
				ShowTaskPane.instance().repaint();
				ShowTaskPane.instance().setVisible(true);

				//int num=indexMap.get(str);
				//int num=map1.get(str);
				//System.out.println("树节点:"+num);
				
				if(count==0){
					ShowTaskPane showTaskPane=map2.get(str);
					TaskCenterPane.instance().addPane(showTaskPane);
					showTaskPane.setAreaText(map.get(str));
					showTaskPane.setNumText(map1.get(str)+"");
					
					update();
					temp=str;
				}
				
				thread=map3.get(str);
				int taskNum=thread.getTaskID();
				if(count>=1&&taskNum<=IPDialog.instance().getIPSum()){
					
					TaskCenterPane.instance().remove(map2.get(temp));
					ShowTaskPane showTaskPane1=map2.get(str);
					TaskCenterPane.instance().addPane(showTaskPane1);
					showTaskPane1.setAreaText(map.get(str));
					showTaskPane1.setNumText(map1.get(str)+"");					

					update();
					temp=str;
				}
				
				else if(taskNum>IPDialog.instance().getIPSum()){
					thread=map3.get(str);
					int taskID=thread.getTaskID();
					System.out.println("任务标识为："+taskID);
					TaskCenterPane.instance().remove(map2.get(temp));
					ShowTaskPane showTaskPane2=map2.get(str);
					TaskCenterPane.instance().addPane(showTaskPane2);
					showTaskPane2.setAreaText(IPDialog.instance().getMap3().get(taskID));
					showTaskPane2.setNumText(taskID+"");					

					update();
					temp=str;
				}  
				count+=1;
			}
		});
	}

	public void update(){
		TaskCenterPane.instance().updateUI();
		TaskCenterPane.instance().validate();
		TaskCenterPane.instance().repaint();
		TaskCenterPane.instance().setVisible(true);
	}
}