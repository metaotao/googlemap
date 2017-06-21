package downloadmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tool.GoogleKey;
import tool.MapParameter;


/**
* @author tao
* version 1.0
*/

public class DownloadRemainImage implements Runnable{
	private String path;
	private String name;
	public static ArrayList<String> list=new ArrayList<String>();
	private String key=null;
	private double end_longitude;
	private double end_latitude;
	private int count=0;
	private ArrayList<String> APIList=new ArrayList<String>();

	public DownloadRemainImage(String path,String name){
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
		URL url=null;
		HttpURLConnection urlConnection=null;
		OutputStream output=null;
		String[] split=null;
		String urlStr=null;
		try{	
			split=name.split(",");

			if(!GoogleKey.isEmpty(APIList)){
				APIList=GoogleKey.load2();
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
			
			String imagepath=path+MapParameter.ZOOM+"-"+split[0]+"//"+split[1]+"-"+split[2]+"-"+split[3]+".png";
			
			if(urlConnection.getResponseCode()==403){
				System.out.println("出现禁止访问错误,重新换密钥");
				key=GoogleKey.getKey(APIList);
				count+=1;
			}

			BufferedImage image=ImageIO.read(urlConnection.getInputStream());
			BufferedImage newImage=new BufferedImage(MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,BufferedImage.TYPE_INT_RGB);
			int[] buf=image.getRGB(0,30,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,new int[MapParameter.MAP_WIDTH*MapParameter.MAP_HEIGHT],0,MapParameter.MAP_WIDTH);				
			newImage.setRGB(0,0,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,buf,0,MapParameter.MAP_WIDTH);
							
			System.out.println(imagepath);
			File file=new File(imagepath);
			output=new FileOutputStream(file);
			ImageIO.write(newImage,"png",output);

			System.out.println("图片"+imagepath+"重新下载成功！");
			
		}
		
		catch(Exception e){
			try{
				Thread.sleep(3000);
			}
			catch(InterruptedException ee){
				ee.printStackTrace();
			}
			
			System.out.println("图片再次重新下载.....");
			DownloadImage imageDownload=new DownloadImage(path,name);	
			//imageDownload.setEnd_longitude(end_longitude);
			//imageDownload.setEnd_latitude(end_latitude);
			new Thread(imageDownload).start();			
			System.out.println(e.getMessage());
		}
		finally{
			try{
				output.close();
				Thread.sleep(3000);				
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

	
}