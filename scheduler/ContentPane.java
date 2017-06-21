package scheduler;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.Map;
import java.util.ArrayList;

import bean.*;

public class ContentPane extends JPanel{
	private JLabel[] ipLabel;
	private JLabel[] clientIPLabel;
	private Map<Integer,String> map;
	private static ContentPane contentPane;
	
	private ClientBean clientBean=new ClientBean();
	private static ArrayList<String> list;

	public static ContentPane instance(){
		if(contentPane==null){
			contentPane=new ContentPane(list);
		}
		return contentPane;
	}

	public ContentPane(ArrayList<String> list){
		this.list=list;
		
		contentPane=this;
		setLayout(null);	
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D)g;
		g2d.setColor(Color.BLUE);
		g2d.fillOval(250,100,26,26);
			
		map=IPDialog.instance().getMap2();
		ipLabel=new JLabel[map.size()];
		
		//list=clientBean.getList();
		clientIPLabel=new JLabel[list.size()];

		for(int i=1;i<=map.size();i++){
			String ip=map.get(i);
			g2d.setColor(Color.RED);
			g2d.fillOval(85*i,200,26,26);

			ipLabel[i-1]=new JLabel();
			ipLabel[i-1].setBounds(85*i,200,26,26);
			add(ipLabel[i-1]);
				
			listener(ipLabel[i-1],ip);
			g2d.setColor(Color.YELLOW);
			g2d.drawLine(263,113,85*i+13,213);
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,16));
			g2d.drawString(i+"",85*i+10,218);
		
			for(int j=1;j<=list.size();j++){
				String[] split=list.get(j-1).split(" ");

				if(ip.equals(split[0])){
					g2d.setColor(Color.GRAY);
					g2d.fillOval(35*j,350,26,26);

					clientIPLabel[j-1]=new JLabel();
					clientIPLabel[j-1].setBounds(35*j,350,26,26);
					add(clientIPLabel[j-1]);
					listener(clientIPLabel[j-1],split[1]);

					g2d.setColor(Color.YELLOW);
					g2d.drawLine(85*i+13,213,35*j+13,363);
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,16));
					g2d.drawString(j+"",35*j+10,368);
				}
			}				
		}
	}

	public void listener(JLabel label,String ip){
		label.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){
					/*JDialog dialog=new JDialog();
					JLabel label=new JLabel(ip);
					dialog.add(label);

					dialog.setLocation(200,250);
					dialog.setSize(500,200);
					dialog.setVisible(true);*/
					//ScreenDialog dialog=new ScreenDialog(ip);
					//new Thread(new ServerThread(ip)).start();
				}
			}

			public void mousePressed(MouseEvent e){
			}

			public void mouseReleased(MouseEvent e){
			}

			public void mouseEntered(MouseEvent e){
			}

			public void mouseExited(MouseEvent e){
			}
		});
	}
}