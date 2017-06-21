package chart;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.*;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class RealTimeChange{  
	private TimeSeries timeSeries;  
	private String title="子蜘蛛线程数量监控";
    private String x_title="时间";
	private String y_title="数量";
	private String d_name="线程数量";
	private double x_time=6*60*1000;
	private double y_count=50;
	
	private static RealTimeChange realTimeChange;
	public static RealTimeChange instance(){
		if(realTimeChange==null){
			realTimeChange=new RealTimeChange();
		}
		return realTimeChange;
	}

    public RealTimeChange(){  
		realTimeChange=this;
		
    }        

	public JPanel createChartPanel(){
		JFreeChart jfreeChart=createChart();
		ChartPanel chartPanel=new ChartPanel(jfreeChart);
		chartPanel.setMouseWheelEnabled(true);
		return chartPanel;
	}

	public void showJFreeChart(double x1,double x2){
		try{
			Millisecond millisecond=new Millisecond();
			timeSeries.add(millisecond,x1);
			timeSeries.add(millisecond,x2);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void showJFreeChart(double x1){
		try{
			Millisecond millisecond=new Millisecond();
			timeSeries.add(millisecond,x1);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

    public JFreeChart createChart(){

		timeSeries=new TimeSeries(d_name);
		TimeSeriesCollection timeSeriesCollection=new TimeSeriesCollection(timeSeries);
		//TimeSeriesCollection.addSeries(timeSeries);

		JFreeChart jfreeChart=ChartFactory.createTimeSeriesChart(title,x_title,
			y_title,timeSeriesCollection,true,true,false);
		setFont(jfreeChart);
		jfreeChart.setBackgroundPaint(Color.WHITE);

        XYPlot xyplot=jfreeChart.getXYPlot();  
		XYLineAndShapeRenderer xylineandshaperenderer=(XYLineAndShapeRenderer)xyplot.getRenderer();
		xyplot.setBackgroundPaint(Color.WHITE);
		xyplot.setDomainGridlinePaint(Color.gray);
		xyplot.setRangeGridlinePaint(Color.gray);
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		xyplot.setAxisOffset(new RectangleInsets(0.0D,0.0D,0.0D,0.0D));

		xylineandshaperenderer.setBaseShapesVisible(true);
		XYItemRenderer xyitem=xyplot.getRenderer();
		//xyitem.setBaseItemLabelsVisible(false);
		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,TextAnchor.BASELINE_LEFT));
		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xyitem.setBaseItemLabelFont(new Font("Dialog",Font.BOLD,14));
		xyplot.setRenderer(xyitem);

		Object localObject=(DateAxis)xyplot.getDomainAxis();
		((DateAxis)localObject).setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
		ValueAxis valueaxis=xyplot.getDomainAxis();
		valueaxis.setAutoRange(true);
		valueaxis.setFixedAutoRange(x_time);
		valueaxis=xyplot.getRangeAxis();
		valueaxis.setRange(0.0D,y_count);

		xyplot.setDataset(0,timeSeriesCollection);
		return jfreeChart;
	}

	public void setFont(JFreeChart jFreeChart){
		TextTitle subtitle=new TextTitle("",new Font("黑体",1,12));
		jFreeChart.addSubtitle(subtitle);
		jFreeChart.setTitle(new TextTitle(title,new Font("微软雅黑",2,15)));
		jFreeChart.setAntiAlias(true);
		XYPlot xyPlot=jFreeChart.getXYPlot();

		ValueAxis valueAxis=xyPlot.getDomainAxis();
		valueAxis.setLabelFont(new Font("黑体",Font.BOLD,12));
		valueAxis.setTickLabelFont(new Font("黑体",Font.BOLD,12));

		NumberAxis numberAxis=(NumberAxis)xyPlot.getRangeAxis();
		numberAxis.setTickLabelFont(new Font("微软雅黑",Font.BOLD,13));
		numberAxis.setLabelFont(new Font("微软雅黑",Font.BOLD,13));

		jFreeChart.getLegend().setItemFont(new Font("微软雅黑",Font.BOLD,13));
	}
}  