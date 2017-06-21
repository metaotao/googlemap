package scheduler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import tool.SystemTool;
/**
* @author tao
* @version 1.0
*/

public class IPTreePane extends JPanel{
	private static IPTreePane ipTree;
	private Map<Integer,String> map;
	private ArrayList<String> list;
	private HeatbeatReceiveThread thread;
	
	public IPTreePane(HeatbeatReceiveThread thread){
		this.thread=thread;
		setLayout(new BorderLayout());
		init();
	}

	public void init(){
		JTree tree=createTree(thread);
	}

	//创建IP节点树
	public JTree createTree(HeatbeatReceiveThread thread){
		String schedulerIP=SystemTool.instance().getSchedulerIP();
		String serverIP=SystemTool.instance().getServerIP();
		DefaultMutableTreeNode node=new DefaultMutableTreeNode(schedulerIP);

		DefaultMutableTreeNode root=new DefaultMutableTreeNode("IP节点树");
		root.add(node);	
		
		map=IPDialog.instance().getMap2();
		list=thread.getList();		

		for(int i=1;i<=map.size();i++){
			String ip=map.get(i);			
			DefaultMutableTreeNode node1=new DefaultMutableTreeNode(ip);
			node.add(node1);
			for(int j=1;j<=list.size();j++){
				String[] split=list.get(j-1).split(" ");
				if(ip.equals(split[0])){
					DefaultMutableTreeNode node2=new DefaultMutableTreeNode(split[0]);
					node1.add(node2);
				}
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

}