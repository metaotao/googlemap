package downloadmap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import clientgui.MainFrame;
import clientgui.ShowPane;
import tool.InfoTool;
import tool.LinkQueue;

public class MonitorDownload implements Runnable {
	private int sum;
	private int taskNum;
	private static final int NTHREADS=25;
	private ExecutorService exec;

	private int completeNum = 0;

	public MonitorDownload() {
	}

	public void run() {
		try {
			Thread.sleep(5000);
			Thread.currentThread().setPriority(8);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		System.out.println("��ͼ���ؿ�ʼ����...");

		// do{
		try {
			exec=Executors.newFixedThreadPool(NTHREADS);
			System.out.println("��ʼ��ȡ����...");
			
			InfoTool.isDownload = true;
			sum = MainFrame.instance().getAllSum();
			Download download = new Download();
			InfoTool.download = download;
			System.out.println("��ʼ���ز�����߳���Ϣ..");
			// infoBean=download.getInfoBean();
			new Thread(download).start();
			ShowPane.instance().setCurrentText("��ȡ����������...");
			Thread.sleep(10*1000);
			do {
				if (InfoTool.download.getExec().isTerminated()) {
					System.out.println("�����߳�ִ�����");
					Thread.sleep(10 * 1000);
					break;
				}
				int threadCount = ((ThreadPoolExecutor) InfoTool.download.getExec()).getActiveCount();
				MainFrame.instance().getRealTimeChange().showJFreeChart(threadCount);
				ShowPane.instance().setExecuteText(threadCount + "");

				if (threadCount >= 10) {
					ShowPane.instance().setStrongText("��");
				} else {
					ShowPane.instance().setStrongText("��");
				}

				if (threadCount == 1 || taskNum >= sum) {
					InfoTool.download.getExec().shutdownNow();
					break;
				}

				Thread.sleep(1000);
				InfoTool.isSleep = false;
				// System.out.println("��ǰ�̳߳�����="+threadCount);
			} while (true);

			InfoTool.isDownload = false;
			completeNum += sum;
			ShowPane.instance().setCompleteText(completeNum + "");
			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			System.out.println("��ʼ����ʣ��ͼ��...");

			/*while (LinkQueue.getUrlNum() > 0&&!exec.isShutdown()) {
				ShowPane.instance().setCurrentText("����ʣ��ͼ��...");
				String obj = (String) LinkQueue.unVisitUrlDeQueue();
				String[] split = obj.split(" ");
				double latitude = Double.parseDouble(split[0]);
				double longitude = Double.parseDouble(split[1]);
				int col = Integer.parseInt(split[2]);
				int row = Integer.parseInt(split[3]);
	
				LimitRepeatDL limitRepeatDL=new LimitRepeatDL(latitude, longitude, col, row);
				exec.execute(limitRepeatDL);				
			}
			
			exec.shutdown();
			while (true) {
				if (exec.isTerminated()) {
					break;
				}
				Thread.sleep(2000);
			}*/

			System.out.println("ʣ��ͼ���������");
			new SendCompleteTaskNum(InfoTool.completeTask);
			InfoTool.completeTask = 0;

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// }while(true);

}