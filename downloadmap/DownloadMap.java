package downloadmap;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import clientgui.ShowPane;
import sql.InsertFileInfoToSql;
import tool.GoogleKey;
import tool.InfoTool;
import tool.LinkQueue;
import tool.MapParameter;
import tool.Transform;

/**
 * @author tao version 1.0
 */

public class DownloadMap implements Runnable {

	private double start_longitude;
	private double start_latitude;
	private double lngStartPixel;
	private double latStartPixel;
	private int col;
	private int rowCount;
	private int count = 0;
	private String key = null;

	private ArrayList<String> APIList = new ArrayList<String>();
	private double lastTime;
	private double downloadTime = 10 * 60 * 1000;
	private int sleepTime = 60 * 1000;

	public DownloadMap(double start_latitude, double start_longitude, int col) {
		this.start_latitude = start_latitude;
		this.start_longitude = start_longitude;
		this.col = col;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void run() {
		try {
			latStartPixel = Transform.latToPixel(start_latitude, MapParameter.ZOOM);
			lngStartPixel = Transform.lngToPixel(start_longitude, MapParameter.ZOOM);

			String filepath = InfoTool.path + col;
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
			}

			lastTime = System.currentTimeMillis();

			for (int j = 1; j <= rowCount; j++) {
				if (System.currentTimeMillis() - lastTime >= downloadTime) {
					// if(InfoTool.isSleep==true){
					try {
						ShowPane.instance().setSleepText("是");
						ShowPane.instance().setCurrentText("正在休眠...");
						Thread.sleep(sleepTime);
						lastTime = System.currentTimeMillis();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				ShowPane.instance().setSleepText("否");
				ShowPane.instance().setCurrentText("正在下载地图...");
				Thread.sleep(100);
				download(Transform.pixelToLat(latStartPixel + MapParameter.MAP_HEIGHT * (j - 1), MapParameter.ZOOM),
						Transform.pixelToLng(lngStartPixel + MapParameter.MAP_HEIGHT * (col - 1), MapParameter.ZOOM),
						col, j);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void download(double latitude, double longitude, int col, int row) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		OutputStream output = null;
		File file = null;
		String urlStr = null;
		try {
			if (!GoogleKey.isEmpty(APIList)) {
				APIList = GoogleKey.load1();
			}

			if (count <= 0) {
				String keyOne = GoogleKey.getKey(APIList);
				urlStr = "http://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude
						+ "&zoom=16&format=png&size=256x316&maptype=satellite&sensor=false&key=" + keyOne;
			} else {
				urlStr = "http://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude
						+ "&zoom=16&format=png&size=256x316&maptype=satellite&sensor=false&key=" + key;

			}

			url = new URL(urlStr);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(5000);
			urlConnection.connect();

			String imagepath = InfoTool.path + col + "\\" + (row + "_" + col) + ".png";

			if (urlConnection.getResponseCode() == 403) {
				System.out.println("出现禁止访问错误,重新换密钥");
				key = GoogleKey.getKey(APIList);
				count += 1;
			}

			BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
			BufferedImage image = ImageIO.read(bis);

			BufferedImage newImage = new BufferedImage(MapParameter.MAP_WIDTH, MapParameter.MAP_HEIGHT,
					BufferedImage.TYPE_INT_RGB);
			int[] buf = image.getRGB(0, 30, MapParameter.MAP_WIDTH, MapParameter.MAP_HEIGHT,
					new int[MapParameter.MAP_WIDTH * MapParameter.MAP_HEIGHT], 0, MapParameter.MAP_WIDTH);
			newImage.setRGB(0, 0, MapParameter.MAP_WIDTH, MapParameter.MAP_HEIGHT, buf, 0, MapParameter.MAP_WIDTH);

			file = new File(imagepath);
			output = new FileOutputStream(file);
			ImageIO.write(newImage, "png", output);
			System.out.println("图片" + imagepath + "下载完成！");
			InsertFileInfoToSql.instance().insertInfo(InfoTool.county_ID, row, longitude, col, latitude);

		} catch (Exception e) {
			try {
				Thread.sleep(2000);

			} catch (InterruptedException ee) {
				Thread.currentThread().interrupt();
			}
			System.out.println(e.getMessage());
		//	LinkQueue.addUnvisitUrl(latitude + " " + longitude + " " + col + " " + row);

		} finally {
			try {
				output.close();
				Thread.sleep(2000);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}