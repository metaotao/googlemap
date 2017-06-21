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

import sql.InsertFileInfoToSql;
import tool.GoogleKey;
import tool.InfoTool;
import tool.MapParameter;

public class LimitRepeatDL implements Runnable{
	private ArrayList<String> APIList = new ArrayList<String>();
	private int count = 0;
	private String key = null;
	
	private double latitude;
	private double longitude;
	private int col;
	private int row;
	public LimitRepeatDL(double latitude, double longitude, int col, int row) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.col = col;
		this.row = row;
	}
	
	@Override
	public void run() {
		download(latitude,longitude,col,row);
		
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

		} finally {
			try {
				output.close();
				Thread.sleep(3000);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
