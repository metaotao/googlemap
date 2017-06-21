
package chart;

import java.io.PrintStream;
import org.jfree.data.time.Millisecond;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TestChart extends ApplicationFrame
	implements Runnable
{

	static Thread thread1;
	private RealTimeChange timeSeriesDemo2;
	private static double lastValue1;
	private static double originalValue1 = 200D;

	public static void main(String args[])
	{
		new TestChart("蜘蛛机线程数量时序图 ");
	}

	public TestChart(String paramString)
	{
		super(paramString);
		thread1 = new Thread(this);
		timeSeriesDemo2 = new RealTimeChange();
		setContentPane(timeSeriesDemo2.createChartPanel());
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
		startThread();
	}

	public static void startThread()
	{
		thread1.start();
	}

	public void run()
	{
		do
			try
			{
				double d1 = 2D * Math.random();
				lastValue1 = originalValue1 * d1;
				Millisecond millisecond1 = new Millisecond();
				System.out.println((new StringBuilder("Series1 Now=")).append(millisecond1.toString()).toString());
				timeSeriesDemo2.showJFreeChart(lastValue1);
				Thread.sleep(1000L);
			}
			catch (InterruptedException interruptedexception) { }
		while (true);
	}

}