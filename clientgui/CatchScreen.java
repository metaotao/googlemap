package clientgui;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.io.ByteArrayOutputStream ;
import java.io.IOException;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.text.SimpleDateFormat;
import java.util.Date;

import bean.*;
import tool.*;
/**
* @author tao
* @version 1.0
*/
public class CatchScreen implements Runnable{
	private Robot robot;
	private Dimension dim;
	private DatagramSocket socket;
	private Rectangle rect;
	private String schedulerIP=SystemTool.instance().getSchedulerIP();
	private int jiankongPort=SystemTool.instance().getJiankongPort();

	private volatile boolean running=false;

	public CatchScreen(DatagramSocket socket){
		this.socket=socket;
	}

	public void run(){
		
		try{
			running=true;
			robot=new Robot();			
			dim=Toolkit.getDefaultToolkit().getScreenSize();
			rect=new Rectangle(dim);
			System.out.println("屏幕截屏发送");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		while(running){						
			try{
				BufferedImage image=robot.createScreenCapture(rect);				
				System.out.println("开始发送截屏图像....");
				byte[] buf=imageToBytes(image);
				System.out.println("*******"+buf.length);

				byte[] by=new byte[32008];
				int length=buf.length;
				int n=0;
				byte[] nu=intToBytes(buf.length);
				while(length>0){
					int size=0;
					if(length>32000){
						size=32000;
					}
					else{
						size=length;
					}
					byte[] id=intToBytes(n);
					System.arraycopy(nu,0,by,0,4);
					System.arraycopy(id,0,by,4,4);
					System.arraycopy(buf,buf.length-length,by,8,size);
					DatagramPacket packet=new DatagramPacket(by,size+8,
						InetAddress.getByName(schedulerIP),jiankongPort);
					socket.send(packet);
					n+=1;
					length=length-size;
				}
				System.out.println("发送完一组图像");
				
			}
			catch(Exception e){
				System.out.println("发送截屏图像出现异常："+e.getMessage());
				stop();
			}
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}			
	}

	public byte[] imageToBytes(BufferedImage image){
		byte[] buf=null;
		try{
			ByteArrayOutputStream imageStream=new ByteArrayOutputStream();
			ImageIO.write(image,"jpg",imageStream);
			buf=imageStream.toByteArray();

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return buf;
	}

	public byte[] intToBytes(int i){
		byte[] data=new byte[4];
		data[0]=(byte)((i>>24)&0xFF);
		data[1]=(byte)((i>>16)&0xFF);
		data[2]=(byte)((i>>8)&0xFF); 
		data[3]=(byte)(i&0xFF);
		return data;
	}

	public void stop(){
		if(running){
			running=false;
		}
		if(socket!=null){
			try{
				socket.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				socket=null;
			}
		}
		System.out.println("关闭客户端");

	}
}
  