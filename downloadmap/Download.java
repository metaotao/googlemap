package downloadmap;

import java.io.File;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clientgui.MainFrame;
import clientgui.ShowPane;
import sql.CreateMapTable;
import sql.GetFileName;
import sql.InsertFileInfoToSql;
import tool.InfoTool;
import bean.MapClientBean;

/**
 * @author tao version 1.0
 */

public class Download implements Runnable {
	private String county;

	private double start_longitude;
	private double start_latitude;
	private double end_longitude;
	private double end_latitude;

	private int colRange;
	private int rowRange;

	private String path;

	private int maxThreadCount = 25;
	// private double endTime;

	private long lastTime;
	private long downloadTime = 10 * 60 * 1000;
	private long sleepTime = 60 * 1000;

	private ExecutorService pool;
	private MapClientBean clientBean;
	private GetFileName getFileName = new GetFileName();
	private ArrayList<Integer> fileNameList;
	private InsertFileInfoToSql insertFile;
	private static Download download;

	public static Download instance() {
		if (download == null) {
			download = new Download();
		}
		return download;
	}

	public Download() {
		download = this;
		// start();
	}

	public void run() {
		try {
			Thread.sleep(5 * 1000);
			Thread.currentThread().setPriority(8);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			pool = Executors.newFixedThreadPool(maxThreadCount);
			// infoBean.setExec(pool);

			clientBean = MainFrame.instance().getClientBean();
			county = clientBean.getCounty();

			start_longitude = clientBean.getStart_longitude();
			start_latitude = clientBean.getStart_latitude();
			end_longitude = clientBean.getEnd_longitude();
			end_latitude = clientBean.getEnd_latitude();
			setEnd_longitude(end_longitude);
			setEnd_latitude(end_latitude);

			fileNameList = getFileName.executeQuery(county);
			int pro_ID = fileNameList.get(0);
			int pro_city_ID = fileNameList.get(1);
			int pro_city_county_ID = fileNameList.get(2);
			path = "E:\\" + pro_ID + "\\" + pro_city_ID + "\\" + pro_city_county_ID + "\\";

			InfoTool.path = path;
			InfoTool.county_ID = pro_city_county_ID;
			
			CreateMapTable.instance().createTable(pro_ID, pro_city_ID, pro_city_county_ID);
			insertFile = new InsertFileInfoToSql(path);

			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}

			colRange = clientBean.getColRange();
			rowRange = clientBean.getRowRange();
			
			lastTime = System.currentTimeMillis();
			for (int col = 1; col <= colRange; col++) {

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

				DownloadMap downloadMap = new DownloadMap(start_latitude, start_longitude, col);
				downloadMap.setRowCount(rowRange);
				pool.execute(downloadMap);
			}
			// 关闭线程池
			pool.shutdown();
			while (true) {
				if (pool.isTerminated()) {
					break;
				}
				Thread.sleep(3000);
			}
		}
		
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExecutorService getExec() {
		return pool;
	}

	public void setEnd_longitude(double end_longitude) {
		this.end_longitude = end_longitude;
	}

	public double getEnd_longitude() {
		return end_longitude;
	}

	public void setEnd_latitude(double end_latitude) {
		this.end_latitude = end_latitude;
	}

	public double getEnd_latitude() {
		return end_latitude;
	}
}