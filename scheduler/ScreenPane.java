package scheduler;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;

public class ScreenPane extends JPanel{
	
	private JLabel label;
	private static ScreenPane screenPane;
	public static ScreenPane instance(){
		if(screenPane==null){
			screenPane=new ScreenPane();
		}
		return screenPane;
	}

	public ScreenPane(){
		screenPane=this;
		setBackground(new Color(240,240,185));		
	}

	public void init(String str){
		label=new JLabel(str);
		label.setFont(new Font("微软雅黑",Font.PLAIN,19));
		add(label);
	}

	public void paint(Image image,Graphics g){
		super.paint(g);
		g.drawImage(image,0,0,this.getWidth(),this.getHeight(),this);
	}

	public void remove(Graphics g){
		super.paint(g);
	}

	public void drawString(Graphics g){
		super.paint(g);
		g.setColor(Color.RED);
		g.setFont(new Font("微软雅黑",Font.PLAIN,16));
		g.drawString("客户端未连接或客户端退出！",50,100);
	}

}
