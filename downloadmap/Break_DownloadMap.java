package downloadmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tool.MapParameter;
import tool.Transform;

/**
* @author tao
* version 1.0
*/

public class Break_DownloadMap implements Runnable{

	private String path;
	private double start_longitude;
	private double start_latitude;
	private double lngStartPixel;
	private double latStartPixel;
	private int row;
	private int col;
	private int rowCount;
	private int count=0;
	private double end_longitude;
	private double end_latitude;
	private String key=null;
	private String[] keys={"AIzaSyB-fImuq9XFz3ZdRwFB-0KkytFkqOCJwzE","AIzaSyCA5UaUMFp2pnYSo6poJiQieWHSHg8mmDk",
		"AIzaSyCNs8J3o4LkpHG7EYdK_QailpYilLuBLOg","AIzaSyAndzSRoAbMJLajU-zrfmDT8b_VB8fTKgg",
		"AIzaSyB8eX8CsVMoXz_L04QO_AY68yxPhgKLdg4","AIzaSyA0A6RORCGxOHp2qS9JBndrlTsS2BQz25w",
		"AIzaSyB3zKVWf9WqTelPtinqxfQ3xDha1NHW6Nk","AIzaSyBTf6Sj4zaP9CwhXDDEACmO4a0IvELj4Dc",
		"AIzaSyCVSDDhSRJEj92EgGB7QbqSGVG9jj5AJqM"};
	
	private ArrayList list;
	private MapParameter map=new MapParameter();
	public Break_DownloadMap(String path,double start_latitude,double start_longitude,int col){
		this.path=path;		
		this.start_latitude=start_latitude;
		this.start_longitude=start_longitude;
		this.col=col;	
	}

	public void setRowCount(int rowCount){
		this.rowCount=rowCount;
	}

	public int getRowCount(){
		return rowCount;
	}

	public void setRow(int row){
		this.row=row;
	}

	public int getRow(){
		return row;
	}


	public void run(){
		try{

			latStartPixel=Transform.latToPixel(start_latitude,MapParameter.ZOOM);			
			lngStartPixel=Transform.lngToPixel(start_longitude,MapParameter.ZOOM);
			
			String filepath=path+MapParameter.ZOOM+"-"+col;
		//	System.out.println("********"+filepath);
			File file=new File(filepath);
			if(!file.exists()){
				file.mkdirs();
			}				
			for(int j=row;j<rowCount;j++){
				download(path,Transform.pixelToLat(latStartPixel+MapParameter.MAP_HEIGHT*j,MapParameter.ZOOM),
					Transform.pixelToLng(lngStartPixel+MapParameter.MAP_HEIGHT*col,MapParameter.ZOOM),col,j);

			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void download(String path,double latitude,double longitude,int col,int row){
		URL url=null;
		
		String urlStr=null;
		HttpURLConnection urlConnection=null;
		OutputStream output=null;
		try{
			if(count<=0){
				key="AIzaSyAndzSRoAbMJLajU-zrfmDT8b_VB8fTKgg";
				urlStr="http://maps.googleapis.com/maps/api/staticmap?center="+latitude+","+longitude+
					"&zoom=18&format=png&size=256x316&maptype=satellite&sensor=false&key="+key;
			}
			else{
				urlStr="http://maps.googleapis.com/maps/api/staticmap?center="+latitude+","+longitude+
				"&zoom=18&format=png&size=256x316&maptype=satellite&sensor=false&key="+key;
			}
			url=new URL(urlStr);			
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(5000);
			
			String imagepath=path+MapParameter.ZOOM+"-"+col+"//"+row+"-"+latitude+"-"+longitude+".png";

			if(urlConnection.getResponseCode()==403){
				System.out.println("出现禁止访问错误,重新换密钥");
				int random=(int)(Math.random()*9);
				System.out.println(urlStr);
				key=keys[random];
				System.out.println("**********"+key);
				count+=1;
			}
			BufferedImage image=ImageIO.read(urlConnection.getInputStream());
			BufferedImage newImage=new BufferedImage(MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,BufferedImage.TYPE_INT_RGB);
			int[] buf=image.getRGB(0,30,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,new int[MapParameter.MAP_WIDTH*MapParameter.MAP_HEIGHT],0,MapParameter.MAP_WIDTH);				
			newImage.setRGB(0,0,MapParameter.MAP_WIDTH,MapParameter.MAP_HEIGHT,buf,0,MapParameter.MAP_WIDTH);
						
			
			File file=new File(imagepath);
			output=new FileOutputStream(file);
			ImageIO.write(newImage,"png",output);
			System.out.println("图片"+imagepath+"下载完成！");

		} 
		catch(Exception e){
			try{
				Thread.sleep(3000);
				
			}
			catch(InterruptedException ee){
				ee.printStackTrace();
			}
			String name=col+","+row+","+latitude+","+longitude;
			System.out.println(col+","+row+","+latitude+","+longitude+"重新下载");
			
			//重新尝试
			DownloadRemainImage downloadRemainImage=new DownloadRemainImage(path,name);
			downloadRemainImage.setEnd_longitude(end_longitude);
			downloadRemainImage.setEnd_latitude(end_latitude);
			Thread thread=new Thread(downloadRemainImage);
			thread.start();
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