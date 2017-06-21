package downloadmap;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;

import sql.*;
import tool.*;

public class DownloadImage implements Runnable{
	private String path;
	private String name;
	private String type;
	private double end_longitude;
	private double end_latitude;
	private int count;
	private String key=null;
	private ArrayList<String> APIList=new ArrayList<String>();

	public DownloadImage(String path,String name){
		this.path=path;
		this.name=name;
	}
	
	public void setEnd_longitude(double end_longitude){
		this.end_longitude=end_longitude;
	}

	public double getEnd_longitude(){
		return end_longitude;
	}

	public void setEnd_latitude(double end_latitude){
		this.end_latitude=end_latitude;
	}

	public double getEnd_latitude(){
		return end_latitude;
	}


	public void run(){
		String[] split=name.split(",");
		URL url=null;
		HttpURLConnection urlConnection=null;
		OutputStream output=null;
		String urlStr=null;
		try{
			if(!GoogleKey.isEmpty(APIList)){
				APIList=GoogleKey.load3();
			}

			if(count<=0){
				String keyOne=GoogleKey.getKey(APIList);
				urlStr="http://maps.googleapis.com/maps/api/staticmap?center="+split[2]+","+split[3]+
					"&zoom=18&format=png&size=256x316&maptype=satellite&sensor=false&key="+keyOne;
			}
			else{
				urlStr="http://maps.googleapis.com/maps/api/staticmap?center="+split[2]+","+split[3]+
				"&zoom=18&format=png&size=256x316&maptype=satellite&sensor=false&key="+key;
			}

			url=new URL(urlStr);			  
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(5000);			
						
			if(urlConnection.getResponseCode()==403){
				System.out.println("出现禁止访问错误,重新换密钥");
				//int random=(int)(Math.random()*8);
				key=GoogleKey.getKey(APIList);
				count+=1;
			}

			String imagepath=path+MapParameter.ZOOM+"-"+split[0]+"//"+split[1]+"-"+split[2]+"-"+split[3]+".png";

			BufferedImage image=ImageIO.read(urlConnection.getInputStream());
			BufferedImage newImage=new BufferedImage(MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,BufferedImage.TYPE_INT_RGB);
			int[] buf=image.getRGB(0,30,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,new int[MapParameter.MAP_WIDTH*MapParameter.MAP_HEIGHT],0,MapParameter.MAP_WIDTH);				
			newImage.setRGB(0,0,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,buf,0,MapParameter.MAP_WIDTH);
										
			File file=new File(imagepath);
			output=new FileOutputStream(file);
			ImageIO.write(newImage,"png",output);
			System.out.println("图片"+imagepath+"  重新  下载完成！");
			type="ok";
		}
		catch(Exception e){
			try{
				Thread.sleep(3000);
			}
			catch(InterruptedException ee){
			//	ee.printStackTrace();
			}
			System.out.println(e.getMessage());

		}
		finally{
			try{
				output.close();
				Thread.sleep(2000);						
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

	public String getType(){
		return type;
	}
}