package scheduler;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Graphics;

/**
* @author tao
* @version 1.0
*/

public class ScreenReceiver implements Runnable{
	private BufferedImage image;
	private DatagramSocket socket;
	private volatile boolean running=false;	
	private boolean continueLoop=true;
	private boolean flag=true;
	private ScreenPane screenPane;

	public ScreenReceiver(DatagramSocket socket,ScreenPane screenPane){
		this.socket=socket;
		this.screenPane=screenPane;
	}

	public void run(){
		if(running){
			return;
		}
		running=true;
		byte[] len=new byte[32000];
		byte[] buffer=new byte[32000];
		byte[] ID=new byte[4];
		int i=0;
		int size=0;
		int id=0;
		int over=0;
		int count=0;
		int length=0;
		BufferedImage bufferImage=null;
		try{	
			while(running&&flag){
				System.out.println("开始接受图像...");
				byte[] imageBuffer=new byte[32008];
				DatagramPacket backPacket=new DatagramPacket(imageBuffer,imageBuffer.length);
				socket.receive(backPacket);
				
				System.arraycopy(imageBuffer,0,len,0,4);
				if(size==0){
					size=bytesToInt(len);
				}
				System.arraycopy(imageBuffer,4,ID,0,4);
				id=bytesToInt(ID);
				int num=backPacket.getLength()-8;
				System.arraycopy(imageBuffer,8,buffer,over,num);
				over=over+num;
				
			/*	if(id==0&&i>0){
					
				}
				while(id>0){
					length=length+buffer.length;
					byte[] b=new byte[length];*/


				if(i==0&&id!=0){
					i--;
					over=0;
					size=0;
				}
				if(over>=size){
					if(i!=id){
						System.out.println("丢包");
						i=-1;
						over=0;
						size=0;
					}
					else{
						i=-1;
						over=0;
						size=0;
						ByteArrayInputStream input=new ByteArrayInputStream(buffer);
						try{

							bufferImage=ImageIO.read(input);
						}
						catch(IOException e){
							e.printStackTrace();
						}

						ImageIcon imageIcon=new ImageIcon(bufferImage);
						Graphics g=screenPane.getGraphics();
						screenPane.remove(g);

						Image image=imageIcon.getImage();				
						image=image.getScaledInstance(screenPane.getWidth(),screenPane.getHeight(),Image.SCALE_DEFAULT);								
						screenPane.paint(image,g);	
					}
				}

				i++;
			}
		}
		catch(IOException e){
			Graphics g=screenPane.getGraphics();
			screenPane.remove(g);
			screenPane.drawString(g);
			shutdownSocket();
		}
		catch(Exception e){
			e.printStackTrace();			
		}
		
	}

	public int bytesToInt(byte[] b){
		int i=0;
		i+=((b[0]&0xff)<<24);  
        i+=((b[1]&0xff)<<16);  
        i+=((b[2]&0xff)<<8);  
        i+=((b[3]&0xff));
		return i;
	}

	public void shutdownSocket(){
		System.out.println("一个客户端主动关闭");
		if(flag){
			flag=false;
		}
	}

}