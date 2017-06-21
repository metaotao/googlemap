// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   XYPlot.java

package org.jfree.chart.plot;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYAnnotationBoundsInfo;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.TickType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.data.Range;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.io.SerialUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectList;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PaintUtilities;
import org.jfree.util.PublicCloneable;

// Referenced classes of package org.jfree.chart.plot:
//			Plot, Marker, CrosshairState, ValueAxisPlot, 
//			Pannable, Zoomable, DatasetRenderingOrder, SeriesRenderingOrder, 
//			PlotOrientation, PlotRenderingInfo, PlotState

public class XYPlot extends Plot
	implements ValueAxisPlot, Pannable, Zoomable, RendererChangeListener, Cloneable, PublicCloneable, Serializable
{

	private static final long serialVersionUID = 0x61c1d7820b208cb0L;
	public static final Stroke DEFAULT_GRIDLINE_STROKE;
	public static final Paint DEFAULT_GRIDLINE_PAINT;
	public static final boolean DEFAULT_CROSSHAIR_VISIBLE = false;
	public static final Stroke DEFAULT_CROSSHAIR_STROKE;
	public static final Paint DEFAULT_CROSSHAIR_PAINT;
	protected static ResourceBundle localizationResources = ResourceBundleWrapper.getBundle("org.jfree.chart.plot.LocalizationBundle");
	private PlotOrientation orientation;
	private RectangleInsets axisOffset;
	private ObjectList domainAxes;
	private ObjectList domainAxisLocations;
	private ObjectList rangeAxes;
	private ObjectList rangeAxisLocations;
	private ObjectList datasets;
	private ObjectList renderers;
	private Map datasetToDomainAxesMap;
	private Map datasetToRangeAxesMap;
	private transient Point2D quadrantOrigin;
	private transient Paint quadrantPaint[] = {
		null, null, null, null
	};
	private boolean domainGridlinesVisible;
	private transient Stroke domainGridlineStroke;
	private transient Paint domainGridlinePaint;
	private boolean rangeGridlinesVisible;
	private transient Stroke rangeGridlineStroke;
	private transient Paint rangeGridlinePaint;
	private boolean domainMinorGridlinesVisible;
	private transient Stroke domainMinorGridlineStroke;
	private transient Paint domainMinorGridlinePaint;
	private boolean rangeMinorGridlinesVisible;
	private transient Stroke rangeMinorGridlineStroke;
	private transient Paint rangeMinorGridlinePaint;
	private boolean domainZeroBaselineVisible;
	private transient Stroke domainZeroBaselineStroke;
	private transient Paint domainZeroBaselinePaint;
	private boolean rangeZeroBaselineVisible;
	private transient Stroke rangeZeroBaselineStroke;
	private transient Paint rangeZeroBaselinePaint;
	private boolean domainCrosshairVisible;
	private double domainCrosshairValue;
	private transient Stroke domainCrosshairStroke;
	private transient Paint domainCrosshairPaint;
	private boolean domainCrosshairLockedOnData;
	private boolean rangeCrosshairVisible;
	private double rangeCrosshairValue;
	private transient Stroke rangeCrosshairStroke;
	private transient Paint rangeCrosshairPaint;
	private boolean rangeCrosshairLockedOnData;
	private Map foregroundDomainMarkers;
	private Map backgroundDomainMarkers;
	private Map foregroundRangeMarkers;
	private Map backgroundRangeMarkers;
	private java.util.List annotations;
	private transient Paint domainTickBandPaint;
	private transient Paint rangeTickBandPaint;
	private AxisSpace fixedDomainAxisSpace;
	private AxisSpace fixedRangeAxisSpace;
	private DatasetRenderingOrder datasetRenderingOrder;
	private SeriesRenderingOrder seriesRenderingOrder;
	private int weight;
	private LegendItemCollection fixedLegendItems;
	private boolean domainPannable;
	private boolean rangePannable;

	public XYPlot()
	{
		this(null, null, null, null);
	}

	public XYPlot(XYDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis, XYItemRenderer renderer)
	{
		quadrantOrigin = new java.awt.geom.Point2D.Double(0.0D, 0.0D);
		domainCrosshairLockedOnData = true;
		rangeCrosshairLockedOnData = true;
		datasetRenderingOrder = DatasetRenderingOrder.REVERSE;
		seriesRenderingOrder = SeriesRenderingOrder.REVERSE;
		orientation = PlotOrientation.VERTICAL;
		weight = 1;
		axisOffset = RectangleInsets.ZERO_INSETS;
		domainAxes = new ObjectList();
		domainAxisLocations = new ObjectList();
		foregroundDomainMarkers = new HashMap();
		backgroundDomainMarkers = new HashMap();
		rangeAxes = new ObjectList();
		rangeAxisLocations = new ObjectList();
		foregroundRangeMarkers = new HashMap();
		backgroundRangeMarkers = new HashMap();
		datasets = new ObjectList();
		renderers = new ObjectList();
		datasetToDomainAxesMap = new TreeMap();
		datasetToRangeAxesMap = new TreeMap();
		annotations = new ArrayList();
		datasets.set(0, dataset);
		if (dataset != null)
			dataset.addChangeListener(this);
		renderers.set(0, renderer);
		if (renderer != null)
		{
			renderer.setPlot(this);
			renderer.addChangeListener(this);
		}
		domainAxes.set(0, domainAxis);
		mapDatasetToDomainAxis(0, 0);
		if (domainAxis != null)
		{
			domainAxis.setPlot(this);
			domainAxis.addChangeListener(this);
		}
		domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
		rangeAxes.set(0, rangeAxis);
		mapDatasetToRangeAxis(0, 0);
		if (rangeAxis != null)
		{
			rangeAxis.setPlot(this);
			rangeAxis.addChangeListener(this);
		}
		rangeAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);
		configureDomainAxes();
		configureRangeAxes();
		domainGridlinesVisible = true;
		domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
		domainGridlinePaint = DEFAULT_GRIDLINE_PAINT;
		domainMinorGridlinesVisible = false;
		domainMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
		domainMinorGridlinePaint = Color.white;
		domainZeroBaselineVisible = false;
		domainZeroBaselinePaint = Color.black;
		domainZeroBaselineStroke = new BasicStroke(0.5F);
		rangeGridlinesVisible = true;
		rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
		rangeGridlinePaint = DEFAULT_GRIDLINE_PAINT;
		rangeMinorGridlinesVisible = false;
		rangeMinorGridlineStroke = DEFAULT_GRIDLINE_STROKE;
		rangeMinorGridlinePaint = Color.white;
		rangeZeroBaselineVisible = false;
		rangeZeroBaselinePaint = Color.black;
		rangeZeroBaselineStroke = new BasicStroke(0.5F);
		domainCrosshairVisible = false;
		domainCrosshairValue = 0.0D;
		domainCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
		domainCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;
		rangeCrosshairVisible = false;
		rangeCrosshairValue = 0.0D;
		rangeCrosshairStroke = DEFAULT_CROSSHAIR_STROKE;
		rangeCrosshairPaint = DEFAULT_CROSSHAIR_PAINT;
	}

	public String getPlotType()
	{
		return localizationResources.getString("XY_Plot");
	}

	public PlotOrientation getOrientation()
	{
		return orientation;
	}

	public void setOrientation(PlotOrientation orientation)
	{
		if (orientation == null)
			throw new IllegalArgumentException("Null 'orientation' argument.");
		if (orientation != this.orientation)
		{
			this.orientation = orientation;
			fireChangeEvent();
		}
	}

	public RectangleInsets getAxisOffset()
	{
		return axisOffset;
	}

	public void setAxisOffset(RectangleInsets offset)
	{
		if (offset == null)
		{
			throw new IllegalArgumentException("Null 'offset' argument.");
		} else
		{
			axisOffset = offset;
			fireChangeEvent();
			return;
		}
	}

	public ValueAxis getDomainAxis()
	{
		return getDomainAxis(0);
	}

	public ValueAxis getDomainAxis(int index)
	{
		ValueAxis result = null;
		if (index < domainAxes.size())
			result = (ValueAxis)domainAxes.get(index);
		if (result == null)
		{
			Plot parent = getParent();
			if (parent instanceof XYPlot)
			{
				XYPlot xy = (XYPlot)parent;
				result = xy.getDomainAxis(index);
			}
		}
		return result;
	}

	public void setDomainAxis(ValueAxis axis)
	{
		setDomainAxis(0, axis);
	}

	public void setDomainAxis(int index, ValueAxis axis)
	{
		setDomainAxis(index, axis, true);
	}

	public void setDomainAxis(int index, ValueAxis axis, boolean notify)
	{
		ValueAxis existing = getDomainAxis(index);
		if (existing != null)
			existing.removeChangeListener(this);
		if (axis != null)
			axis.setPlot(this);
		domainAxes.set(index, axis);
		if (axis != null)
		{
			axis.configure();
			axis.addChangeListener(this);
		}
		if (notify)
			fireChangeEvent();
	}

	public void setDomainAxes(ValueAxis axes[])
	{
		for (int i = 0; i < axes.length; i++)
			setDomainAxis(i, axes[i], false);

		fireChangeEvent();
	}

	public AxisLocation getDomainAxisLocation()
	{
		return (AxisLocation)domainAxisLocations.get(0);
	}

	public void setDomainAxisLocation(AxisLocation location)
	{
		setDomainAxisLocation(0, location, true);
	}

	public void setDomainAxisLocation(AxisLocation location, boolean notify)
	{
		setDomainAxisLocation(0, location, notify);
	}

	public RectangleEdge getDomainAxisEdge()
	{
		return Plot.resolveDomainAxisLocation(getDomainAxisLocation(), orientation);
	}

	public int getDomainAxisCount()
	{
		return domainAxes.size();
	}

	public void clearDomainAxes()
	{
		for (int i = 0; i < domainAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)domainAxes.get(i);
			if (axis != null)
				axis.removeChangeListener(this);
		}

		domainAxes.clear();
		fireChangeEvent();
	}

	public void configureDomainAxes()
	{
		for (int i = 0; i < domainAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)domainAxes.get(i);
			if (axis != null)
				axis.configure();
		}

	}

	public AxisLocation getDomainAxisLocation(int index)
	{
		AxisLocation result = null;
		if (index < domainAxisLocations.size())
			result = (AxisLocation)domainAxisLocations.get(index);
		if (result == null)
			result = AxisLocation.getOpposite(getDomainAxisLocation());
		return result;
	}

	public void setDomainAxisLocation(int index, AxisLocation location)
	{
		setDomainAxisLocation(index, location, true);
	}

	public void setDomainAxisLocation(int index, AxisLocation location, boolean notify)
	{
		if (index == 0 && location == null)
			throw new IllegalArgumentException("Null 'location' for index 0 not permitted.");
		domainAxisLocations.set(index, location);
		if (notify)
			fireChangeEvent();
	}

	public RectangleEdge getDomainAxisEdge(int index)
	{
		AxisLocation location = getDomainAxisLocation(index);
		RectangleEdge result = Plot.resolveDomainAxisLocation(location, orientation);
		if (result == null)
			result = RectangleEdge.opposite(getDomainAxisEdge());
		return result;
	}

	public ValueAxis getRangeAxis()
	{
		return getRangeAxis(0);
	}

	public void setRangeAxis(ValueAxis axis)
	{
		if (axis != null)
			axis.setPlot(this);
		ValueAxis existing = getRangeAxis();
		if (existing != null)
			existing.removeChangeListener(this);
		rangeAxes.set(0, axis);
		if (axis != null)
		{
			axis.configure();
			axis.addChangeListener(this);
		}
		fireChangeEvent();
	}

	public AxisLocation getRangeAxisLocation()
	{
		return (AxisLocation)rangeAxisLocations.get(0);
	}

	public void setRangeAxisLocation(AxisLocation location)
	{
		setRangeAxisLocation(0, location, true);
	}

	public void setRangeAxisLocation(AxisLocation location, boolean notify)
	{
		setRangeAxisLocation(0, location, notify);
	}

	public RectangleEdge getRangeAxisEdge()
	{
		return Plot.resolveRangeAxisLocation(getRangeAxisLocation(), orientation);
	}

	public ValueAxis getRangeAxis(int index)
	{
		ValueAxis result = null;
		if (index < rangeAxes.size())
			result = (ValueAxis)rangeAxes.get(index);
		if (result == null)
		{
			Plot parent = getParent();
			if (parent instanceof XYPlot)
			{
				XYPlot xy = (XYPlot)parent;
				result = xy.getRangeAxis(index);
			}
		}
		return result;
	}

	public void setRangeAxis(int index, ValueAxis axis)
	{
		setRangeAxis(index, axis, true);
	}

	public void setRangeAxis(int index, ValueAxis axis, boolean notify)
	{
		ValueAxis existing = getRangeAxis(index);
		if (existing != null)
			existing.removeChangeListener(this);
		if (axis != null)
			axis.setPlot(this);
		rangeAxes.set(index, axis);
		if (axis != null)
		{
			axis.configure();
			axis.addChangeListener(this);
		}
		if (notify)
			fireChangeEvent();
	}

	public void setRangeAxes(ValueAxis axes[])
	{
		for (int i = 0; i < axes.length; i++)
			setRangeAxis(i, axes[i], false);

		fireChangeEvent();
	}

	public int getRangeAxisCount()
	{
		return rangeAxes.size();
	}

	public void clearRangeAxes()
	{
		for (int i = 0; i < rangeAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)rangeAxes.get(i);
			if (axis != null)
				axis.removeChangeListener(this);
		}

		rangeAxes.clear();
		fireChangeEvent();
	}

	public void configureRangeAxes()
	{
		for (int i = 0; i < rangeAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)rangeAxes.get(i);
			if (axis != null)
				axis.configure();
		}

	}

	public AxisLocation getRangeAxisLocation(int index)
	{
		AxisLocation result = null;
		if (index < rangeAxisLocations.size())
			result = (AxisLocation)rangeAxisLocations.get(index);
		if (result == null)
			result = AxisLocation.getOpposite(getRangeAxisLocation());
		return result;
	}

	public void setRangeAxisLocation(int index, AxisLocation location)
	{
		setRangeAxisLocation(index, location, true);
	}

	public void setRangeAxisLocation(int index, AxisLocation location, boolean notify)
	{
		if (index == 0 && location == null)
			throw new IllegalArgumentException("Null 'location' for index 0 not permitted.");
		rangeAxisLocations.set(index, location);
		if (notify)
			fireChangeEvent();
	}

	public RectangleEdge getRangeAxisEdge(int index)
	{
		AxisLocation location = getRangeAxisLocation(index);
		RectangleEdge result = Plot.resolveRangeAxisLocation(location, orientation);
		if (result == null)
			result = RectangleEdge.opposite(getRangeAxisEdge());
		return result;
	}

	public XYDataset getDataset()
	{
		return getDataset(0);
	}

	public XYDataset getDataset(int index)
	{
		XYDataset result = null;
		if (datasets.size() > index)
			result = (XYDataset)datasets.get(index);
		return result;
	}

	public void setDataset(XYDataset dataset)
	{
		setDataset(0, dataset);
	}

	public void setDataset(int index, XYDataset dataset)
	{
		XYDataset existing = getDataset(index);
		if (existing != null)
			existing.removeChangeListener(this);
		datasets.set(index, dataset);
		if (dataset != null)
			dataset.addChangeListener(this);
		DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
		datasetChanged(event);
	}

	public int getDatasetCount()
	{
		return datasets.size();
	}

	public int indexOf(XYDataset dataset)
	{
		int result = -1;
		int i = 0;
		do
		{
			if (i >= datasets.size())
				break;
			if (dataset == datasets.get(i))
			{
				result = i;
				break;
			}
			i++;
		} while (true);
		return result;
	}

	public void mapDatasetToDomainAxis(int index, int axisIndex)
	{
		java.util.List axisIndices = new ArrayList(1);
		axisIndices.add(new Integer(axisIndex));
		mapDatasetToDomainAxes(index, axisIndices);
	}

	public void mapDatasetToDomainAxes(int index, java.util.List axisIndices)
	{
		if (index < 0)
		{
			throw new IllegalArgumentException("Requires 'index' >= 0.");
		} else
		{
			checkAxisIndices(axisIndices);
			Integer key = new Integer(index);
			datasetToDomainAxesMap.put(key, new ArrayList(axisIndices));
			datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
			return;
		}
	}

	public void mapDatasetToRangeAxis(int index, int axisIndex)
	{
		java.util.List axisIndices = new ArrayList(1);
		axisIndices.add(new Integer(axisIndex));
		mapDatasetToRangeAxes(index, axisIndices);
	}

	public void mapDatasetToRangeAxes(int index, java.util.List axisIndices)
	{
		if (index < 0)
		{
			throw new IllegalArgumentException("Requires 'index' >= 0.");
		} else
		{
			checkAxisIndices(axisIndices);
			Integer key = new Integer(index);
			datasetToRangeAxesMap.put(key, new ArrayList(axisIndices));
			datasetChanged(new DatasetChangeEvent(this, getDataset(index)));
			return;
		}
	}

	private void checkAxisIndices(java.util.List indices)
	{
		if (indices == null)
			return;
		int count = indices.size();
		if (count == 0)
			throw new IllegalArgumentException("Empty list not permitted.");
		HashSet set = new HashSet();
		for (int i = 0; i < count; i++)
		{
			Object item = indices.get(i);
			if (!(item instanceof Integer))
				throw new IllegalArgumentException("Indices must be Integer instances.");
			if (set.contains(item))
				throw new IllegalArgumentException("Indices must be unique.");
			set.add(item);
		}

	}

	public int getRendererCount()
	{
		return renderers.size();
	}

	public XYItemRenderer getRenderer()
	{
		return getRenderer(0);
	}

	public XYItemRenderer getRenderer(int index)
	{
		XYItemRenderer result = null;
		if (renderers.size() > index)
			result = (XYItemRenderer)renderers.get(index);
		return result;
	}

	public void setRenderer(XYItemRenderer renderer)
	{
		setRenderer(0, renderer);
	}

	public void setRenderer(int index, XYItemRenderer renderer)
	{
		setRenderer(index, renderer, true);
	}

	public void setRenderer(int index, XYItemRenderer renderer, boolean notify)
	{
		XYItemRenderer existing = getRenderer(index);
		if (existing != null)
			existing.removeChangeListener(this);
		renderers.set(index, renderer);
		if (renderer != null)
		{
			renderer.setPlot(this);
			renderer.addChangeListener(this);
		}
		configureDomainAxes();
		configureRangeAxes();
		if (notify)
			fireChangeEvent();
	}

	public void setRenderers(XYItemRenderer renderers[])
	{
		for (int i = 0; i < renderers.length; i++)
			setRenderer(i, renderers[i], false);

		fireChangeEvent();
	}

	public DatasetRenderingOrder getDatasetRenderingOrder()
	{
		return datasetRenderingOrder;
	}

	public void setDatasetRenderingOrder(DatasetRenderingOrder order)
	{
		if (order == null)
		{
			throw new IllegalArgumentException("Null 'order' argument.");
		} else
		{
			datasetRenderingOrder = order;
			fireChangeEvent();
			return;
		}
	}

	public SeriesRenderingOrder getSeriesRenderingOrder()
	{
		return seriesRenderingOrder;
	}

	public void setSeriesRenderingOrder(SeriesRenderingOrder order)
	{
		if (order == null)
		{
			throw new IllegalArgumentException("Null 'order' argument.");
		} else
		{
			seriesRenderingOrder = order;
			fireChangeEvent();
			return;
		}
	}

	public int getIndexOf(XYItemRenderer renderer)
	{
		return renderers.indexOf(renderer);
	}

	public XYItemRenderer getRendererForDataset(XYDataset dataset)
	{
		XYItemRenderer result = null;
		int i = 0;
		do
		{
			if (i >= datasets.size())
				break;
			if (datasets.get(i) == dataset)
			{
				result = (XYItemRenderer)renderers.get(i);
				if (result == null)
					result = getRenderer();
				break;
			}
			i++;
		} while (true);
		return result;
	}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
		fireChangeEvent();
	}

	public boolean isDomainGridlinesVisible()
	{
		return domainGridlinesVisible;
	}

	public void setDomainGridlinesVisible(boolean visible)
	{
		if (domainGridlinesVisible != visible)
		{
			domainGridlinesVisible = visible;
			fireChangeEvent();
		}
	}

	public boolean isDomainMinorGridlinesVisible()
	{
		return domainMinorGridlinesVisible;
	}

	public void setDomainMinorGridlinesVisible(boolean visible)
	{
		if (domainMinorGridlinesVisible != visible)
		{
			domainMinorGridlinesVisible = visible;
			fireChangeEvent();
		}
	}

	public Stroke getDomainGridlineStroke()
	{
		return domainGridlineStroke;
	}

	public void setDomainGridlineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			domainGridlineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Stroke getDomainMinorGridlineStroke()
	{
		return domainMinorGridlineStroke;
	}

	public void setDomainMinorGridlineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			domainMinorGridlineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getDomainGridlinePaint()
	{
		return domainGridlinePaint;
	}

	public void setDomainGridlinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			domainGridlinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public Paint getDomainMinorGridlinePaint()
	{
		return domainMinorGridlinePaint;
	}

	public void setDomainMinorGridlinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			domainMinorGridlinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public boolean isRangeGridlinesVisible()
	{
		return rangeGridlinesVisible;
	}

	public void setRangeGridlinesVisible(boolean visible)
	{
		if (rangeGridlinesVisible != visible)
		{
			rangeGridlinesVisible = visible;
			fireChangeEvent();
		}
	}

	public Stroke getRangeGridlineStroke()
	{
		return rangeGridlineStroke;
	}

	public void setRangeGridlineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			rangeGridlineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getRangeGridlinePaint()
	{
		return rangeGridlinePaint;
	}

	public void setRangeGridlinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			rangeGridlinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public boolean isRangeMinorGridlinesVisible()
	{
		return rangeMinorGridlinesVisible;
	}

	public void setRangeMinorGridlinesVisible(boolean visible)
	{
		if (rangeMinorGridlinesVisible != visible)
		{
			rangeMinorGridlinesVisible = visible;
			fireChangeEvent();
		}
	}

	public Stroke getRangeMinorGridlineStroke()
	{
		return rangeMinorGridlineStroke;
	}

	public void setRangeMinorGridlineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			rangeMinorGridlineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getRangeMinorGridlinePaint()
	{
		return rangeMinorGridlinePaint;
	}

	public void setRangeMinorGridlinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			rangeMinorGridlinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public boolean isDomainZeroBaselineVisible()
	{
		return domainZeroBaselineVisible;
	}

	public void setDomainZeroBaselineVisible(boolean visible)
	{
		domainZeroBaselineVisible = visible;
		fireChangeEvent();
	}

	public Stroke getDomainZeroBaselineStroke()
	{
		return domainZeroBaselineStroke;
	}

	public void setDomainZeroBaselineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			domainZeroBaselineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getDomainZeroBaselinePaint()
	{
		return domainZeroBaselinePaint;
	}

	public void setDomainZeroBaselinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			domainZeroBaselinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public boolean isRangeZeroBaselineVisible()
	{
		return rangeZeroBaselineVisible;
	}

	public void setRangeZeroBaselineVisible(boolean visible)
	{
		rangeZeroBaselineVisible = visible;
		fireChangeEvent();
	}

	public Stroke getRangeZeroBaselineStroke()
	{
		return rangeZeroBaselineStroke;
	}

	public void setRangeZeroBaselineStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			rangeZeroBaselineStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getRangeZeroBaselinePaint()
	{
		return rangeZeroBaselinePaint;
	}

	public void setRangeZeroBaselinePaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			rangeZeroBaselinePaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public Paint getDomainTickBandPaint()
	{
		return domainTickBandPaint;
	}

	public void setDomainTickBandPaint(Paint paint)
	{
		domainTickBandPaint = paint;
		fireChangeEvent();
	}

	public Paint getRangeTickBandPaint()
	{
		return rangeTickBandPaint;
	}

	public void setRangeTickBandPaint(Paint paint)
	{
		rangeTickBandPaint = paint;
		fireChangeEvent();
	}

	public Point2D getQuadrantOrigin()
	{
		return quadrantOrigin;
	}

	public void setQuadrantOrigin(Point2D origin)
	{
		if (origin == null)
		{
			throw new IllegalArgumentException("Null 'origin' argument.");
		} else
		{
			quadrantOrigin = origin;
			fireChangeEvent();
			return;
		}
	}

	public Paint getQuadrantPaint(int index)
	{
		if (index < 0 || index > 3)
			throw new IllegalArgumentException("The index value (" + index + ") should be in the range 0 to 3.");
		else
			return quadrantPaint[index];
	}

	public void setQuadrantPaint(int index, Paint paint)
	{
		if (index < 0 || index > 3)
		{
			throw new IllegalArgumentException("The index value (" + index + ") should be in the range 0 to 3.");
		} else
		{
			quadrantPaint[index] = paint;
			fireChangeEvent();
			return;
		}
	}

	public void addDomainMarker(Marker marker)
	{
		addDomainMarker(marker, Layer.FOREGROUND);
	}

	public void addDomainMarker(Marker marker, Layer layer)
	{
		addDomainMarker(0, marker, layer);
	}

	public void clearDomainMarkers()
	{
		if (backgroundDomainMarkers != null)
		{
			Set keys = backgroundDomainMarkers.keySet();
			Integer key;
			for (Iterator iterator = keys.iterator(); iterator.hasNext(); clearDomainMarkers(key.intValue()))
				key = (Integer)iterator.next();

			backgroundDomainMarkers.clear();
		}
		if (foregroundDomainMarkers != null)
		{
			Set keys = foregroundDomainMarkers.keySet();
			Integer key;
			for (Iterator iterator = keys.iterator(); iterator.hasNext(); clearDomainMarkers(key.intValue()))
				key = (Integer)iterator.next();

			foregroundDomainMarkers.clear();
		}
		fireChangeEvent();
	}

	public void clearDomainMarkers(int index)
	{
		Integer key = new Integer(index);
		if (backgroundDomainMarkers != null)
		{
			Collection markers = (Collection)backgroundDomainMarkers.get(key);
			if (markers != null)
			{
				Marker m;
				for (Iterator iterator = markers.iterator(); iterator.hasNext(); m.removeChangeListener(this))
					m = (Marker)iterator.next();

				markers.clear();
			}
		}
		if (foregroundRangeMarkers != null)
		{
			Collection markers = (Collection)foregroundDomainMarkers.get(key);
			if (markers != null)
			{
				Marker m;
				for (Iterator iterator = markers.iterator(); iterator.hasNext(); m.removeChangeListener(this))
					m = (Marker)iterator.next();

				markers.clear();
			}
		}
		fireChangeEvent();
	}

	public void addDomainMarker(int index, Marker marker, Layer layer)
	{
		addDomainMarker(index, marker, layer, true);
	}

	public void addDomainMarker(int index, Marker marker, Layer layer, boolean notify)
	{
		if (marker == null)
			throw new IllegalArgumentException("Null 'marker' not permitted.");
		if (layer == null)
			throw new IllegalArgumentException("Null 'layer' not permitted.");
		if (layer == Layer.FOREGROUND)
		{
			Collection markers = (Collection)foregroundDomainMarkers.get(new Integer(index));
			if (markers == null)
			{
				markers = new ArrayList();
				foregroundDomainMarkers.put(new Integer(index), markers);
			}
			markers.add(marker);
		} else
		if (layer == Layer.BACKGROUND)
		{
			Collection markers = (Collection)backgroundDomainMarkers.get(new Integer(index));
			if (markers == null)
			{
				markers = new ArrayList();
				backgroundDomainMarkers.put(new Integer(index), markers);
			}
			markers.add(marker);
		}
		marker.addChangeListener(this);
		if (notify)
			fireChangeEvent();
	}

	public boolean removeDomainMarker(Marker marker)
	{
		return removeDomainMarker(marker, Layer.FOREGROUND);
	}

	public boolean removeDomainMarker(Marker marker, Layer layer)
	{
		return removeDomainMarker(0, marker, layer);
	}

	public boolean removeDomainMarker(int index, Marker marker, Layer layer)
	{
		return removeDomainMarker(index, marker, layer, true);
	}

	public boolean removeDomainMarker(int index, Marker marker, Layer layer, boolean notify)
	{
		ArrayList markers;
		if (layer == Layer.FOREGROUND)
			markers = (ArrayList)foregroundDomainMarkers.get(new Integer(index));
		else
			markers = (ArrayList)backgroundDomainMarkers.get(new Integer(index));
		if (markers == null)
			return false;
		boolean removed = markers.remove(marker);
		if (removed && notify)
			fireChangeEvent();
		return removed;
	}

	public void addRangeMarker(Marker marker)
	{
		addRangeMarker(marker, Layer.FOREGROUND);
	}

	public void addRangeMarker(Marker marker, Layer layer)
	{
		addRangeMarker(0, marker, layer);
	}

	public void clearRangeMarkers()
	{
		if (backgroundRangeMarkers != null)
		{
			Set keys = backgroundRangeMarkers.keySet();
			Integer key;
			for (Iterator iterator = keys.iterator(); iterator.hasNext(); clearRangeMarkers(key.intValue()))
				key = (Integer)iterator.next();

			backgroundRangeMarkers.clear();
		}
		if (foregroundRangeMarkers != null)
		{
			Set keys = foregroundRangeMarkers.keySet();
			Integer key;
			for (Iterator iterator = keys.iterator(); iterator.hasNext(); clearRangeMarkers(key.intValue()))
				key = (Integer)iterator.next();

			foregroundRangeMarkers.clear();
		}
		fireChangeEvent();
	}

	public void addRangeMarker(int index, Marker marker, Layer layer)
	{
		addRangeMarker(index, marker, layer, true);
	}

	public void addRangeMarker(int index, Marker marker, Layer layer, boolean notify)
	{
		if (layer == Layer.FOREGROUND)
		{
			Collection markers = (Collection)foregroundRangeMarkers.get(new Integer(index));
			if (markers == null)
			{
				markers = new ArrayList();
				foregroundRangeMarkers.put(new Integer(index), markers);
			}
			markers.add(marker);
		} else
		if (layer == Layer.BACKGROUND)
		{
			Collection markers = (Collection)backgroundRangeMarkers.get(new Integer(index));
			if (markers == null)
			{
				markers = new ArrayList();
				backgroundRangeMarkers.put(new Integer(index), markers);
			}
			markers.add(marker);
		}
		marker.addChangeListener(this);
		if (notify)
			fireChangeEvent();
	}

	public void clearRangeMarkers(int index)
	{
		Integer key = new Integer(index);
		if (backgroundRangeMarkers != null)
		{
			Collection markers = (Collection)backgroundRangeMarkers.get(key);
			if (markers != null)
			{
				Marker m;
				for (Iterator iterator = markers.iterator(); iterator.hasNext(); m.removeChangeListener(this))
					m = (Marker)iterator.next();

				markers.clear();
			}
		}
		if (foregroundRangeMarkers != null)
		{
			Collection markers = (Collection)foregroundRangeMarkers.get(key);
			if (markers != null)
			{
				Marker m;
				for (Iterator iterator = markers.iterator(); iterator.hasNext(); m.removeChangeListener(this))
					m = (Marker)iterator.next();

				markers.clear();
			}
		}
		fireChangeEvent();
	}

	public boolean removeRangeMarker(Marker marker)
	{
		return removeRangeMarker(marker, Layer.FOREGROUND);
	}

	public boolean removeRangeMarker(Marker marker, Layer layer)
	{
		return removeRangeMarker(0, marker, layer);
	}

	public boolean removeRangeMarker(int index, Marker marker, Layer layer)
	{
		return removeRangeMarker(index, marker, layer, true);
	}

	public boolean removeRangeMarker(int index, Marker marker, Layer layer, boolean notify)
	{
		if (marker == null)
			throw new IllegalArgumentException("Null 'marker' argument.");
		ArrayList markers;
		if (layer == Layer.FOREGROUND)
			markers = (ArrayList)foregroundRangeMarkers.get(new Integer(index));
		else
			markers = (ArrayList)backgroundRangeMarkers.get(new Integer(index));
		if (markers == null)
			return false;
		boolean removed = markers.remove(marker);
		if (removed && notify)
			fireChangeEvent();
		return removed;
	}

	public void addAnnotation(XYAnnotation annotation)
	{
		addAnnotation(annotation, true);
	}

	public void addAnnotation(XYAnnotation annotation, boolean notify)
	{
		if (annotation == null)
			throw new IllegalArgumentException("Null 'annotation' argument.");
		annotations.add(annotation);
		if (notify)
			fireChangeEvent();
	}

	public boolean removeAnnotation(XYAnnotation annotation)
	{
		return removeAnnotation(annotation, true);
	}

	public boolean removeAnnotation(XYAnnotation annotation, boolean notify)
	{
		if (annotation == null)
			throw new IllegalArgumentException("Null 'annotation' argument.");
		boolean removed = annotations.remove(annotation);
		if (removed && notify)
			fireChangeEvent();
		return removed;
	}

	public java.util.List getAnnotations()
	{
		return new ArrayList(annotations);
	}

	public void clearAnnotations()
	{
		annotations.clear();
		fireChangeEvent();
	}

	protected AxisSpace calculateAxisSpace(Graphics2D g2, Rectangle2D plotArea)
	{
		AxisSpace space = new AxisSpace();
		space = calculateRangeAxisSpace(g2, plotArea, space);
		Rectangle2D revPlotArea = space.shrink(plotArea, null);
		space = calculateDomainAxisSpace(g2, revPlotArea, space);
		return space;
	}

	protected AxisSpace calculateDomainAxisSpace(Graphics2D g2, Rectangle2D plotArea, AxisSpace space)
	{
		if (space == null)
			space = new AxisSpace();
		if (fixedDomainAxisSpace != null)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
			{
				space.ensureAtLeast(fixedDomainAxisSpace.getLeft(), RectangleEdge.LEFT);
				space.ensureAtLeast(fixedDomainAxisSpace.getRight(), RectangleEdge.RIGHT);
			} else
			if (orientation == PlotOrientation.VERTICAL)
			{
				space.ensureAtLeast(fixedDomainAxisSpace.getTop(), RectangleEdge.TOP);
				space.ensureAtLeast(fixedDomainAxisSpace.getBottom(), RectangleEdge.BOTTOM);
			}
		} else
		{
			for (int i = 0; i < domainAxes.size(); i++)
			{
				Axis axis = (Axis)domainAxes.get(i);
				if (axis != null)
				{
					RectangleEdge edge = getDomainAxisEdge(i);
					space = axis.reserveSpace(g2, this, plotArea, edge, space);
				}
			}

		}
		return space;
	}

	protected AxisSpace calculateRangeAxisSpace(Graphics2D g2, Rectangle2D plotArea, AxisSpace space)
	{
		if (space == null)
			space = new AxisSpace();
		if (fixedRangeAxisSpace != null)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
			{
				space.ensureAtLeast(fixedRangeAxisSpace.getTop(), RectangleEdge.TOP);
				space.ensureAtLeast(fixedRangeAxisSpace.getBottom(), RectangleEdge.BOTTOM);
			} else
			if (orientation == PlotOrientation.VERTICAL)
			{
				space.ensureAtLeast(fixedRangeAxisSpace.getLeft(), RectangleEdge.LEFT);
				space.ensureAtLeast(fixedRangeAxisSpace.getRight(), RectangleEdge.RIGHT);
			}
		} else
		{
			for (int i = 0; i < rangeAxes.size(); i++)
			{
				Axis axis = (Axis)rangeAxes.get(i);
				if (axis != null)
				{
					RectangleEdge edge = getRangeAxisEdge(i);
					space = axis.reserveSpace(g2, this, plotArea, edge, space);
				}
			}

		}
		return space;
	}

	public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info)
	{
		boolean b1 = area.getWidth() <= 10D;
		boolean b2 = area.getHeight() <= 10D;
		if (b1 || b2)
			return;
		if (info != null)
			info.setPlotArea(area);
		RectangleInsets insets = getInsets();
		insets.trim(area);
		AxisSpace space = calculateAxisSpace(g2, area);
		Rectangle2D dataArea = space.shrink(area, null);
		axisOffset.trim(dataArea);
		createAndAddEntity((Rectangle2D)dataArea.clone(), info, null, null);
		if (info != null)
			info.setDataArea(dataArea);
		drawBackground(g2, dataArea);
		Map axisStateMap = drawAxes(g2, area, dataArea, info);
		PlotOrientation orient = getOrientation();
		if (anchor != null && !dataArea.contains(anchor))
			anchor = null;
		CrosshairState crosshairState = new CrosshairState();
		crosshairState.setCrosshairDistance((1.0D / 0.0D));
		crosshairState.setAnchor(anchor);
		crosshairState.setAnchorX((0.0D / 0.0D));
		crosshairState.setAnchorY((0.0D / 0.0D));
		if (anchor != null)
		{
			ValueAxis domainAxis = getDomainAxis();
			if (domainAxis != null)
			{
				double x;
				if (orient == PlotOrientation.VERTICAL)
					x = domainAxis.java2DToValue(anchor.getX(), dataArea, getDomainAxisEdge());
				else
					x = domainAxis.java2DToValue(anchor.getY(), dataArea, getDomainAxisEdge());
				crosshairState.setAnchorX(x);
			}
			ValueAxis rangeAxis = getRangeAxis();
			if (rangeAxis != null)
			{
				double y;
				if (orient == PlotOrientation.VERTICAL)
					y = rangeAxis.java2DToValue(anchor.getY(), dataArea, getRangeAxisEdge());
				else
					y = rangeAxis.java2DToValue(anchor.getX(), dataArea, getRangeAxisEdge());
				crosshairState.setAnchorY(y);
			}
		}
		crosshairState.setCrosshairX(getDomainCrosshairValue());
		crosshairState.setCrosshairY(getRangeCrosshairValue());
		java.awt.Shape originalClip = g2.getClip();
		java.awt.Composite originalComposite = g2.getComposite();
		g2.clip(dataArea);
		g2.setComposite(AlphaComposite.getInstance(3, getForegroundAlpha()));
		AxisState domainAxisState = (AxisState)axisStateMap.get(getDomainAxis());
		if (domainAxisState == null && parentState != null)
			domainAxisState = (AxisState)parentState.getSharedAxisStates().get(getDomainAxis());
		AxisState rangeAxisState = (AxisState)axisStateMap.get(getRangeAxis());
		if (rangeAxisState == null && parentState != null)
			rangeAxisState = (AxisState)parentState.getSharedAxisStates().get(getRangeAxis());
		if (domainAxisState != null)
			drawDomainTickBands(g2, dataArea, domainAxisState.getTicks());
		if (rangeAxisState != null)
			drawRangeTickBands(g2, dataArea, rangeAxisState.getTicks());
		if (domainAxisState != null)
		{
			drawDomainGridlines(g2, dataArea, domainAxisState.getTicks());
			drawZeroDomainBaseline(g2, dataArea);
		}
		if (rangeAxisState != null)
		{
			drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
			drawZeroRangeBaseline(g2, dataArea);
		}
		for (int i = 0; i < renderers.size(); i++)
			drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);

		for (int i = 0; i < renderers.size(); i++)
			drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);

		boolean foundData = false;
		DatasetRenderingOrder order = getDatasetRenderingOrder();
		if (order == DatasetRenderingOrder.FORWARD)
		{
			int rendererCount = renderers.size();
			for (int i = 0; i < rendererCount; i++)
			{
				XYItemRenderer r = getRenderer(i);
				if (r != null)
				{
					ValueAxis domainAxis = getDomainAxisForDataset(i);
					ValueAxis rangeAxis = getRangeAxisForDataset(i);
					r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
				}
			}

			for (int i = 0; i < getDatasetCount(); i++)
				foundData = render(g2, dataArea, i, info, crosshairState) || foundData;

			for (int i = 0; i < rendererCount; i++)
			{
				XYItemRenderer r = getRenderer(i);
				if (r != null)
				{
					ValueAxis domainAxis = getDomainAxisForDataset(i);
					ValueAxis rangeAxis = getRangeAxisForDataset(i);
					r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
				}
			}

		} else
		if (order == DatasetRenderingOrder.REVERSE)
		{
			int rendererCount = renderers.size();
			for (int i = rendererCount - 1; i >= 0; i--)
			{
				XYItemRenderer r = getRenderer(i);
				if (i < getDatasetCount() && r != null)
				{
					ValueAxis domainAxis = getDomainAxisForDataset(i);
					ValueAxis rangeAxis = getRangeAxisForDataset(i);
					r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.BACKGROUND, info);
				}
			}

			for (int i = getDatasetCount() - 1; i >= 0; i--)
				foundData = render(g2, dataArea, i, info, crosshairState) || foundData;

			for (int i = rendererCount - 1; i >= 0; i--)
			{
				XYItemRenderer r = getRenderer(i);
				if (i < getDatasetCount() && r != null)
				{
					ValueAxis domainAxis = getDomainAxisForDataset(i);
					ValueAxis rangeAxis = getRangeAxisForDataset(i);
					r.drawAnnotations(g2, dataArea, domainAxis, rangeAxis, Layer.FOREGROUND, info);
				}
			}

		}
		int xAxisIndex = crosshairState.getDomainAxisIndex();
		ValueAxis xAxis = getDomainAxis(xAxisIndex);
		RectangleEdge xAxisEdge = getDomainAxisEdge(xAxisIndex);
		if (!domainCrosshairLockedOnData && anchor != null)
		{
			double xx;
			if (orient == PlotOrientation.VERTICAL)
				xx = xAxis.java2DToValue(anchor.getX(), dataArea, xAxisEdge);
			else
				xx = xAxis.java2DToValue(anchor.getY(), dataArea, xAxisEdge);
			crosshairState.setCrosshairX(xx);
		}
		setDomainCrosshairValue(crosshairState.getCrosshairX(), false);
		if (isDomainCrosshairVisible())
		{
			double x = getDomainCrosshairValue();
			Paint paint = getDomainCrosshairPaint();
			Stroke stroke = getDomainCrosshairStroke();
			drawDomainCrosshair(g2, dataArea, orient, x, xAxis, stroke, paint);
		}
		int yAxisIndex = crosshairState.getRangeAxisIndex();
		ValueAxis yAxis = getRangeAxis(yAxisIndex);
		RectangleEdge yAxisEdge = getRangeAxisEdge(yAxisIndex);
		if (!rangeCrosshairLockedOnData && anchor != null)
		{
			double yy;
			if (orient == PlotOrientation.VERTICAL)
				yy = yAxis.java2DToValue(anchor.getY(), dataArea, yAxisEdge);
			else
				yy = yAxis.java2DToValue(anchor.getX(), dataArea, yAxisEdge);
			crosshairState.setCrosshairY(yy);
		}
		setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
		if (isRangeCrosshairVisible())
		{
			double y = getRangeCrosshairValue();
			Paint paint = getRangeCrosshairPaint();
			Stroke stroke = getRangeCrosshairStroke();
			drawRangeCrosshair(g2, dataArea, orient, y, yAxis, stroke, paint);
		}
		if (!foundData)
			drawNoDataMessage(g2, dataArea);
		for (int i = 0; i < renderers.size(); i++)
			drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);

		for (int i = 0; i < renderers.size(); i++)
			drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);

		drawAnnotations(g2, dataArea, info);
		g2.setClip(originalClip);
		g2.setComposite(originalComposite);
		drawOutline(g2, dataArea);
	}

	public void drawBackground(Graphics2D g2, Rectangle2D area)
	{
		fillBackground(g2, area, orientation);
		drawQuadrants(g2, area);
		drawBackgroundImage(g2, area);
	}

	protected void drawQuadrants(Graphics2D g2, Rectangle2D area)
	{
		boolean somethingToDraw = false;
		ValueAxis xAxis = getDomainAxis();
		if (xAxis == null)
			return;
		double x = xAxis.getRange().constrain(quadrantOrigin.getX());
		double xx = xAxis.valueToJava2D(x, area, getDomainAxisEdge());
		ValueAxis yAxis = getRangeAxis();
		if (yAxis == null)
			return;
		double y = yAxis.getRange().constrain(quadrantOrigin.getY());
		double yy = yAxis.valueToJava2D(y, area, getRangeAxisEdge());
		double xmin = xAxis.getLowerBound();
		double xxmin = xAxis.valueToJava2D(xmin, area, getDomainAxisEdge());
		double xmax = xAxis.getUpperBound();
		double xxmax = xAxis.valueToJava2D(xmax, area, getDomainAxisEdge());
		double ymin = yAxis.getLowerBound();
		double yymin = yAxis.valueToJava2D(ymin, area, getRangeAxisEdge());
		double ymax = yAxis.getUpperBound();
		double yymax = yAxis.valueToJava2D(ymax, area, getRangeAxisEdge());
		Rectangle2D r[] = {
			null, null, null, null
		};
		if (quadrantPaint[0] != null && x > xmin && y < ymax)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
				r[0] = new java.awt.geom.Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmin, xx), Math.abs(yy - yymax), Math.abs(xx - xxmin));
			else
				r[0] = new java.awt.geom.Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymax, yy), Math.abs(xx - xxmin), Math.abs(yy - yymax));
			somethingToDraw = true;
		}
		if (quadrantPaint[1] != null && x < xmax && y < ymax)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
				r[1] = new java.awt.geom.Rectangle2D.Double(Math.min(yymax, yy), Math.min(xxmax, xx), Math.abs(yy - yymax), Math.abs(xx - xxmax));
			else
				r[1] = new java.awt.geom.Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymax, yy), Math.abs(xx - xxmax), Math.abs(yy - yymax));
			somethingToDraw = true;
		}
		if (quadrantPaint[2] != null && x > xmin && y > ymin)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
				r[2] = new java.awt.geom.Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmin, xx), Math.abs(yy - yymin), Math.abs(xx - xxmin));
			else
				r[2] = new java.awt.geom.Rectangle2D.Double(Math.min(xxmin, xx), Math.min(yymin, yy), Math.abs(xx - xxmin), Math.abs(yy - yymin));
			somethingToDraw = true;
		}
		if (quadrantPaint[3] != null && x < xmax && y > ymin)
		{
			if (orientation == PlotOrientation.HORIZONTAL)
				r[3] = new java.awt.geom.Rectangle2D.Double(Math.min(yymin, yy), Math.min(xxmax, xx), Math.abs(yy - yymin), Math.abs(xx - xxmax));
			else
				r[3] = new java.awt.geom.Rectangle2D.Double(Math.min(xx, xxmax), Math.min(yymin, yy), Math.abs(xx - xxmax), Math.abs(yy - yymin));
			somethingToDraw = true;
		}
		if (somethingToDraw)
		{
			java.awt.Composite originalComposite = g2.getComposite();
			g2.setComposite(AlphaComposite.getInstance(3, getBackgroundAlpha()));
			for (int i = 0; i < 4; i++)
				if (quadrantPaint[i] != null && r[i] != null)
				{
					g2.setPaint(quadrantPaint[i]);
					g2.fill(r[i]);
				}

			g2.setComposite(originalComposite);
		}
	}

	public void drawDomainTickBands(Graphics2D g2, Rectangle2D dataArea, java.util.List ticks)
	{
		Paint bandPaint = getDomainTickBandPaint();
		if (bandPaint != null)
		{
			boolean fillBand = false;
			ValueAxis xAxis = getDomainAxis();
			double previous = xAxis.getLowerBound();
			for (Iterator iterator = ticks.iterator(); iterator.hasNext();)
			{
				ValueTick tick = (ValueTick)iterator.next();
				double current = tick.getValue();
				if (fillBand)
					getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, current);
				previous = current;
				fillBand = !fillBand;
			}

			double end = xAxis.getUpperBound();
			if (fillBand)
				getRenderer().fillDomainGridBand(g2, this, xAxis, dataArea, previous, end);
		}
	}

	public void drawRangeTickBands(Graphics2D g2, Rectangle2D dataArea, java.util.List ticks)
	{
		Paint bandPaint = getRangeTickBandPaint();
		if (bandPaint != null)
		{
			boolean fillBand = false;
			ValueAxis axis = getRangeAxis();
			double previous = axis.getLowerBound();
			for (Iterator iterator = ticks.iterator(); iterator.hasNext();)
			{
				ValueTick tick = (ValueTick)iterator.next();
				double current = tick.getValue();
				if (fillBand)
					getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, current);
				previous = current;
				fillBand = !fillBand;
			}

			double end = axis.getUpperBound();
			if (fillBand)
				getRenderer().fillRangeGridBand(g2, this, axis, dataArea, previous, end);
		}
	}

	protected Map drawAxes(Graphics2D g2, Rectangle2D plotArea, Rectangle2D dataArea, PlotRenderingInfo plotState)
	{
		AxisCollection axisCollection = new AxisCollection();
		for (int index = 0; index < domainAxes.size(); index++)
		{
			ValueAxis axis = (ValueAxis)domainAxes.get(index);
			if (axis != null)
				axisCollection.add(axis, getDomainAxisEdge(index));
		}

		for (int index = 0; index < rangeAxes.size(); index++)
		{
			ValueAxis yAxis = (ValueAxis)rangeAxes.get(index);
			if (yAxis != null)
				axisCollection.add(yAxis, getRangeAxisEdge(index));
		}

		Map axisStateMap = new HashMap();
		double cursor = dataArea.getMinY() - axisOffset.calculateTopOutset(dataArea.getHeight());
		ValueAxis axis;
		AxisState info;
		for (Iterator iterator = axisCollection.getAxesAtTop().iterator(); iterator.hasNext(); axisStateMap.put(axis, info))
		{
			axis = (ValueAxis)iterator.next();
			info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.TOP, plotState);
			cursor = info.getCursor();
		}

		cursor = dataArea.getMaxY() + axisOffset.calculateBottomOutset(dataArea.getHeight());
		ValueAxis axis;
		AxisState info;
		for (Iterator iterator = axisCollection.getAxesAtBottom().iterator(); iterator.hasNext(); axisStateMap.put(axis, info))
		{
			axis = (ValueAxis)iterator.next();
			info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.BOTTOM, plotState);
			cursor = info.getCursor();
		}

		cursor = dataArea.getMinX() - axisOffset.calculateLeftOutset(dataArea.getWidth());
		ValueAxis axis;
		AxisState info;
		for (Iterator iterator = axisCollection.getAxesAtLeft().iterator(); iterator.hasNext(); axisStateMap.put(axis, info))
		{
			axis = (ValueAxis)iterator.next();
			info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.LEFT, plotState);
			cursor = info.getCursor();
		}

		cursor = dataArea.getMaxX() + axisOffset.calculateRightOutset(dataArea.getWidth());
		ValueAxis axis;
		AxisState info;
		for (Iterator iterator = axisCollection.getAxesAtRight().iterator(); iterator.hasNext(); axisStateMap.put(axis, info))
		{
			axis = (ValueAxis)iterator.next();
			info = axis.draw(g2, cursor, plotArea, dataArea, RectangleEdge.RIGHT, plotState);
			cursor = info.getCursor();
		}

		return axisStateMap;
	}

	public boolean render(Graphics2D g2, Rectangle2D dataArea, int index, PlotRenderingInfo info, CrosshairState crosshairState)
	{
		boolean foundData = false;
		XYDataset dataset = getDataset(index);
		if (!DatasetUtilities.isEmptyOrNull(dataset))
		{
			foundData = true;
			ValueAxis xAxis = getDomainAxisForDataset(index);
			ValueAxis yAxis = getRangeAxisForDataset(index);
			if (xAxis == null || yAxis == null)
				return foundData;
			XYItemRenderer renderer = getRenderer(index);
			if (renderer == null)
			{
				renderer = getRenderer();
				if (renderer == null)
					return foundData;
			}
			XYItemRendererState state = renderer.initialise(g2, dataArea, this, dataset, info);
			int passCount = renderer.getPassCount();
			SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
			if (seriesOrder == SeriesRenderingOrder.REVERSE)
			{
				for (int pass = 0; pass < passCount; pass++)
				{
					int seriesCount = dataset.getSeriesCount();
					for (int series = seriesCount - 1; series >= 0; series--)
					{
						int firstItem = 0;
						int lastItem = dataset.getItemCount(series) - 1;
						if (lastItem == -1)
							continue;
						if (state.getProcessVisibleItemsOnly())
						{
							int itemBounds[] = RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
							firstItem = Math.max(itemBounds[0] - 1, 0);
							lastItem = Math.min(itemBounds[1] + 1, lastItem);
						}
						state.startSeriesPass(dataset, series, firstItem, lastItem, pass, passCount);
						for (int item = firstItem; item <= lastItem; item++)
							renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);

						state.endSeriesPass(dataset, series, firstItem, lastItem, pass, passCount);
					}

				}

			} else
			{
				for (int pass = 0; pass < passCount; pass++)
				{
					int seriesCount = dataset.getSeriesCount();
					for (int series = 0; series < seriesCount; series++)
					{
						int firstItem = 0;
						int lastItem = dataset.getItemCount(series) - 1;
						if (state.getProcessVisibleItemsOnly())
						{
							int itemBounds[] = RendererUtilities.findLiveItems(dataset, series, xAxis.getLowerBound(), xAxis.getUpperBound());
							firstItem = Math.max(itemBounds[0] - 1, 0);
							lastItem = Math.min(itemBounds[1] + 1, lastItem);
						}
						state.startSeriesPass(dataset, series, firstItem, lastItem, pass, passCount);
						for (int item = firstItem; item <= lastItem; item++)
							renderer.drawItem(g2, state, dataArea, info, this, xAxis, yAxis, dataset, series, item, crosshairState, pass);

						state.endSeriesPass(dataset, series, firstItem, lastItem, pass, passCount);
					}

				}

			}
		}
		return foundData;
	}

	public ValueAxis getDomainAxisForDataset(int index)
	{
		int upper = Math.max(getDatasetCount(), getRendererCount());
		if (index < 0 || index >= upper)
			throw new IllegalArgumentException("Index " + index + " out of bounds.");
		ValueAxis valueAxis = null;
		java.util.List axisIndices = (java.util.List)datasetToDomainAxesMap.get(new Integer(index));
		if (axisIndices != null)
		{
			Integer axisIndex = (Integer)axisIndices.get(0);
			valueAxis = getDomainAxis(axisIndex.intValue());
		} else
		{
			valueAxis = getDomainAxis(0);
		}
		return valueAxis;
	}

	public ValueAxis getRangeAxisForDataset(int index)
	{
		int upper = Math.max(getDatasetCount(), getRendererCount());
		if (index < 0 || index >= upper)
			throw new IllegalArgumentException("Index " + index + " out of bounds.");
		ValueAxis valueAxis = null;
		java.util.List axisIndices = (java.util.List)datasetToRangeAxesMap.get(new Integer(index));
		if (axisIndices != null)
		{
			Integer axisIndex = (Integer)axisIndices.get(0);
			valueAxis = getRangeAxis(axisIndex.intValue());
		} else
		{
			valueAxis = getRangeAxis(0);
		}
		return valueAxis;
	}

	protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea, java.util.List ticks)
	{
		if (getRenderer() == null)
			return;
		if (isDomainGridlinesVisible() || isDomainMinorGridlinesVisible())
		{
			Stroke gridStroke = null;
			Paint gridPaint = null;
			Iterator iterator = ticks.iterator();
			boolean paintLine = false;
			do
			{
				if (!iterator.hasNext())
					break;
				paintLine = false;
				ValueTick tick = (ValueTick)iterator.next();
				if (tick.getTickType() == TickType.MINOR && isDomainMinorGridlinesVisible())
				{
					gridStroke = getDomainMinorGridlineStroke();
					gridPaint = getDomainMinorGridlinePaint();
					paintLine = true;
				} else
				if (tick.getTickType() == TickType.MAJOR && isDomainGridlinesVisible())
				{
					gridStroke = getDomainGridlineStroke();
					gridPaint = getDomainGridlinePaint();
					paintLine = true;
				}
				XYItemRenderer r = getRenderer();
				if ((r instanceof AbstractXYItemRenderer) && paintLine)
					((AbstractXYItemRenderer)r).drawDomainLine(g2, this, getDomainAxis(), dataArea, tick.getValue(), gridPaint, gridStroke);
			} while (true);
		}
	}

	protected void drawRangeGridlines(Graphics2D g2, Rectangle2D area, java.util.List ticks)
	{
		if (getRenderer() == null)
			return;
		if (isRangeGridlinesVisible() || isRangeMinorGridlinesVisible())
		{
			Stroke gridStroke = null;
			Paint gridPaint = null;
			ValueAxis axis = getRangeAxis();
			if (axis != null)
			{
				Iterator iterator = ticks.iterator();
				boolean paintLine = false;
				do
				{
					if (!iterator.hasNext())
						break;
					paintLine = false;
					ValueTick tick = (ValueTick)iterator.next();
					if (tick.getTickType() == TickType.MINOR && isRangeMinorGridlinesVisible())
					{
						gridStroke = getRangeMinorGridlineStroke();
						gridPaint = getRangeMinorGridlinePaint();
						paintLine = true;
					} else
					if (tick.getTickType() == TickType.MAJOR && isRangeGridlinesVisible())
					{
						gridStroke = getRangeGridlineStroke();
						gridPaint = getRangeGridlinePaint();
						paintLine = true;
					}
					if ((tick.getValue() != 0.0D || !isRangeZeroBaselineVisible()) && paintLine)
						getRenderer().drawRangeLine(g2, this, getRangeAxis(), area, tick.getValue(), gridPaint, gridStroke);
				} while (true);
			}
		}
	}

	protected void drawZeroDomainBaseline(Graphics2D g2, Rectangle2D area)
	{
		if (isDomainZeroBaselineVisible())
		{
			XYItemRenderer r = getRenderer();
			if (r instanceof AbstractXYItemRenderer)
			{
				AbstractXYItemRenderer renderer = (AbstractXYItemRenderer)r;
				renderer.drawDomainLine(g2, this, getDomainAxis(), area, 0.0D, domainZeroBaselinePaint, domainZeroBaselineStroke);
			}
		}
	}

	protected void drawZeroRangeBaseline(Graphics2D g2, Rectangle2D area)
	{
		if (isRangeZeroBaselineVisible())
			getRenderer().drawRangeLine(g2, this, getRangeAxis(), area, 0.0D, rangeZeroBaselinePaint, rangeZeroBaselineStroke);
	}

	public void drawAnnotations(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info)
	{
		XYAnnotation annotation;
		ValueAxis xAxis;
		ValueAxis yAxis;
		for (Iterator iterator = annotations.iterator(); iterator.hasNext(); annotation.draw(g2, this, dataArea, xAxis, yAxis, 0, info))
		{
			annotation = (XYAnnotation)iterator.next();
			xAxis = getDomainAxis();
			yAxis = getRangeAxis();
		}

	}

	protected void drawDomainMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer)
	{
		XYItemRenderer r = getRenderer(index);
		if (r == null)
			return;
		if (index >= getDatasetCount())
			return;
		Collection markers = getDomainMarkers(index, layer);
		ValueAxis axis = getDomainAxisForDataset(index);
		if (markers != null && axis != null)
		{
			Marker marker;
			for (Iterator iterator = markers.iterator(); iterator.hasNext(); r.drawDomainMarker(g2, this, axis, marker, dataArea))
				marker = (Marker)iterator.next();

		}
	}

	protected void drawRangeMarkers(Graphics2D g2, Rectangle2D dataArea, int index, Layer layer)
	{
		XYItemRenderer r = getRenderer(index);
		if (r == null)
			return;
		if (index >= getDatasetCount())
			return;
		Collection markers = getRangeMarkers(index, layer);
		ValueAxis axis = getRangeAxisForDataset(index);
		if (markers != null && axis != null)
		{
			Marker marker;
			for (Iterator iterator = markers.iterator(); iterator.hasNext(); r.drawRangeMarker(g2, this, axis, marker, dataArea))
				marker = (Marker)iterator.next();

		}
	}

	public Collection getDomainMarkers(Layer layer)
	{
		return getDomainMarkers(0, layer);
	}

	public Collection getRangeMarkers(Layer layer)
	{
		return getRangeMarkers(0, layer);
	}

	public Collection getDomainMarkers(int index, Layer layer)
	{
		Collection result = null;
		Integer key = new Integer(index);
		if (layer == Layer.FOREGROUND)
			result = (Collection)foregroundDomainMarkers.get(key);
		else
		if (layer == Layer.BACKGROUND)
			result = (Collection)backgroundDomainMarkers.get(key);
		if (result != null)
			result = Collections.unmodifiableCollection(result);
		return result;
	}

	public Collection getRangeMarkers(int index, Layer layer)
	{
		Collection result = null;
		Integer key = new Integer(index);
		if (layer == Layer.FOREGROUND)
			result = (Collection)foregroundRangeMarkers.get(key);
		else
		if (layer == Layer.BACKGROUND)
			result = (Collection)backgroundRangeMarkers.get(key);
		if (result != null)
			result = Collections.unmodifiableCollection(result);
		return result;
	}

	protected void drawHorizontalLine(Graphics2D g2, Rectangle2D dataArea, double value, Stroke stroke, Paint paint)
	{
		ValueAxis axis = getRangeAxis();
		if (getOrientation() == PlotOrientation.HORIZONTAL)
			axis = getDomainAxis();
		if (axis.getRange().contains(value))
		{
			double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
			Line2D line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
			g2.setStroke(stroke);
			g2.setPaint(paint);
			g2.draw(line);
		}
	}

	protected void drawDomainCrosshair(Graphics2D g2, Rectangle2D dataArea, PlotOrientation orientation, double value, ValueAxis axis, Stroke stroke, 
			Paint paint)
	{
		if (axis.getRange().contains(value))
		{
			Line2D line = null;
			if (orientation == PlotOrientation.VERTICAL)
			{
				double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
				line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
			} else
			{
				double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
				line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
			}
			g2.setStroke(stroke);
			g2.setPaint(paint);
			g2.draw(line);
		}
	}

	protected void drawVerticalLine(Graphics2D g2, Rectangle2D dataArea, double value, Stroke stroke, Paint paint)
	{
		ValueAxis axis = getDomainAxis();
		if (getOrientation() == PlotOrientation.HORIZONTAL)
			axis = getRangeAxis();
		if (axis.getRange().contains(value))
		{
			double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
			Line2D line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
			g2.setStroke(stroke);
			g2.setPaint(paint);
			g2.draw(line);
		}
	}

	protected void drawRangeCrosshair(Graphics2D g2, Rectangle2D dataArea, PlotOrientation orientation, double value, ValueAxis axis, Stroke stroke, 
			Paint paint)
	{
		if (axis.getRange().contains(value))
		{
			Line2D line = null;
			if (orientation == PlotOrientation.HORIZONTAL)
			{
				double xx = axis.valueToJava2D(value, dataArea, RectangleEdge.BOTTOM);
				line = new java.awt.geom.Line2D.Double(xx, dataArea.getMinY(), xx, dataArea.getMaxY());
			} else
			{
				double yy = axis.valueToJava2D(value, dataArea, RectangleEdge.LEFT);
				line = new java.awt.geom.Line2D.Double(dataArea.getMinX(), yy, dataArea.getMaxX(), yy);
			}
			g2.setStroke(stroke);
			g2.setPaint(paint);
			g2.draw(line);
		}
	}

	public void handleClick(int x, int y, PlotRenderingInfo info)
	{
		Rectangle2D dataArea = info.getDataArea();
		if (dataArea.contains(x, y))
		{
			ValueAxis xaxis = getDomainAxis();
			if (xaxis != null)
			{
				double hvalue = xaxis.java2DToValue(x, info.getDataArea(), getDomainAxisEdge());
				setDomainCrosshairValue(hvalue);
			}
			ValueAxis yaxis = getRangeAxis();
			if (yaxis != null)
			{
				double vvalue = yaxis.java2DToValue(y, info.getDataArea(), getRangeAxisEdge());
				setRangeCrosshairValue(vvalue);
			}
		}
	}

	private java.util.List getDatasetsMappedToDomainAxis(Integer axisIndex)
	{
		if (axisIndex == null)
			throw new IllegalArgumentException("Null 'axisIndex' argument.");
		java.util.List result = new ArrayList();
		for (int i = 0; i < datasets.size(); i++)
		{
			java.util.List mappedAxes = (java.util.List)datasetToDomainAxesMap.get(new Integer(i));
			if (mappedAxes == null)
			{
				if (axisIndex.equals(ZERO))
					result.add(datasets.get(i));
				continue;
			}
			if (mappedAxes.contains(axisIndex))
				result.add(datasets.get(i));
		}

		return result;
	}

	private java.util.List getDatasetsMappedToRangeAxis(Integer axisIndex)
	{
		if (axisIndex == null)
			throw new IllegalArgumentException("Null 'axisIndex' argument.");
		java.util.List result = new ArrayList();
		for (int i = 0; i < datasets.size(); i++)
		{
			java.util.List mappedAxes = (java.util.List)datasetToRangeAxesMap.get(new Integer(i));
			if (mappedAxes == null)
			{
				if (axisIndex.equals(ZERO))
					result.add(datasets.get(i));
				continue;
			}
			if (mappedAxes.contains(axisIndex))
				result.add(datasets.get(i));
		}

		return result;
	}

	public int getDomainAxisIndex(ValueAxis axis)
	{
		int result = domainAxes.indexOf(axis);
		if (result < 0)
		{
			Plot parent = getParent();
			if (parent instanceof XYPlot)
			{
				XYPlot p = (XYPlot)parent;
				result = p.getDomainAxisIndex(axis);
			}
		}
		return result;
	}

	public int getRangeAxisIndex(ValueAxis axis)
	{
		int result = rangeAxes.indexOf(axis);
		if (result < 0)
		{
			Plot parent = getParent();
			if (parent instanceof XYPlot)
			{
				XYPlot p = (XYPlot)parent;
				result = p.getRangeAxisIndex(axis);
			}
		}
		return result;
	}

	public Range getDataRange(ValueAxis axis)
	{
		Range result = null;
		java.util.List mappedDatasets = new ArrayList();
		java.util.List includedAnnotations = new ArrayList();
		boolean isDomainAxis = true;
		int domainIndex = getDomainAxisIndex(axis);
		if (domainIndex >= 0)
		{
			isDomainAxis = true;
			mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
			if (domainIndex == 0)
			{
				Iterator iterator = annotations.iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					XYAnnotation annotation = (XYAnnotation)iterator.next();
					if (annotation instanceof XYAnnotationBoundsInfo)
						includedAnnotations.add(annotation);
				} while (true);
			}
		}
		int rangeIndex = getRangeAxisIndex(axis);
		if (rangeIndex >= 0)
		{
			isDomainAxis = false;
			mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
			if (rangeIndex == 0)
			{
				Iterator iterator = annotations.iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					XYAnnotation annotation = (XYAnnotation)iterator.next();
					if (annotation instanceof XYAnnotationBoundsInfo)
						includedAnnotations.add(annotation);
				} while (true);
			}
		}
		for (Iterator iterator = mappedDatasets.iterator(); iterator.hasNext();)
		{
			XYDataset d = (XYDataset)iterator.next();
			if (d != null)
			{
				XYItemRenderer r = getRendererForDataset(d);
				if (isDomainAxis)
				{
					if (r != null)
						result = Range.combine(result, r.findDomainBounds(d));
					else
						result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
				} else
				if (r != null)
					result = Range.combine(result, r.findRangeBounds(d));
				else
					result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
				if (r instanceof AbstractXYItemRenderer)
				{
					AbstractXYItemRenderer rr = (AbstractXYItemRenderer)r;
					Collection c = rr.getAnnotations();
					Iterator i = c.iterator();
					while (i.hasNext()) 
					{
						XYAnnotation a = (XYAnnotation)i.next();
						if (a instanceof XYAnnotationBoundsInfo)
							includedAnnotations.add(a);
					}
				}
			}
		}

		Iterator it = includedAnnotations.iterator();
		do
		{
			if (!it.hasNext())
				break;
			XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo)it.next();
			if (xyabi.getIncludeInDataBounds())
				if (isDomainAxis)
					result = Range.combine(result, xyabi.getXRange());
				else
					result = Range.combine(result, xyabi.getYRange());
		} while (true);
		return result;
	}

	public void datasetChanged(DatasetChangeEvent event)
	{
		configureDomainAxes();
		configureRangeAxes();
		if (getParent() != null)
		{
			getParent().datasetChanged(event);
		} else
		{
			PlotChangeEvent e = new PlotChangeEvent(this);
			e.setType(ChartChangeEventType.DATASET_UPDATED);
			notifyListeners(e);
		}
	}

	public void rendererChanged(RendererChangeEvent event)
	{
		if (event.getSeriesVisibilityChanged())
		{
			configureDomainAxes();
			configureRangeAxes();
		}
		fireChangeEvent();
	}

	public boolean isDomainCrosshairVisible()
	{
		return domainCrosshairVisible;
	}

	public void setDomainCrosshairVisible(boolean flag)
	{
		if (domainCrosshairVisible != flag)
		{
			domainCrosshairVisible = flag;
			fireChangeEvent();
		}
	}

	public boolean isDomainCrosshairLockedOnData()
	{
		return domainCrosshairLockedOnData;
	}

	public void setDomainCrosshairLockedOnData(boolean flag)
	{
		if (domainCrosshairLockedOnData != flag)
		{
			domainCrosshairLockedOnData = flag;
			fireChangeEvent();
		}
	}

	public double getDomainCrosshairValue()
	{
		return domainCrosshairValue;
	}

	public void setDomainCrosshairValue(double value)
	{
		setDomainCrosshairValue(value, true);
	}

	public void setDomainCrosshairValue(double value, boolean notify)
	{
		domainCrosshairValue = value;
		if (isDomainCrosshairVisible() && notify)
			fireChangeEvent();
	}

	public Stroke getDomainCrosshairStroke()
	{
		return domainCrosshairStroke;
	}

	public void setDomainCrosshairStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			domainCrosshairStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getDomainCrosshairPaint()
	{
		return domainCrosshairPaint;
	}

	public void setDomainCrosshairPaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			domainCrosshairPaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public boolean isRangeCrosshairVisible()
	{
		return rangeCrosshairVisible;
	}

	public void setRangeCrosshairVisible(boolean flag)
	{
		if (rangeCrosshairVisible != flag)
		{
			rangeCrosshairVisible = flag;
			fireChangeEvent();
		}
	}

	public boolean isRangeCrosshairLockedOnData()
	{
		return rangeCrosshairLockedOnData;
	}

	public void setRangeCrosshairLockedOnData(boolean flag)
	{
		if (rangeCrosshairLockedOnData != flag)
		{
			rangeCrosshairLockedOnData = flag;
			fireChangeEvent();
		}
	}

	public double getRangeCrosshairValue()
	{
		return rangeCrosshairValue;
	}

	public void setRangeCrosshairValue(double value)
	{
		setRangeCrosshairValue(value, true);
	}

	public void setRangeCrosshairValue(double value, boolean notify)
	{
		rangeCrosshairValue = value;
		if (isRangeCrosshairVisible() && notify)
			fireChangeEvent();
	}

	public Stroke getRangeCrosshairStroke()
	{
		return rangeCrosshairStroke;
	}

	public void setRangeCrosshairStroke(Stroke stroke)
	{
		if (stroke == null)
		{
			throw new IllegalArgumentException("Null 'stroke' argument.");
		} else
		{
			rangeCrosshairStroke = stroke;
			fireChangeEvent();
			return;
		}
	}

	public Paint getRangeCrosshairPaint()
	{
		return rangeCrosshairPaint;
	}

	public void setRangeCrosshairPaint(Paint paint)
	{
		if (paint == null)
		{
			throw new IllegalArgumentException("Null 'paint' argument.");
		} else
		{
			rangeCrosshairPaint = paint;
			fireChangeEvent();
			return;
		}
	}

	public AxisSpace getFixedDomainAxisSpace()
	{
		return fixedDomainAxisSpace;
	}

	public void setFixedDomainAxisSpace(AxisSpace space)
	{
		setFixedDomainAxisSpace(space, true);
	}

	public void setFixedDomainAxisSpace(AxisSpace space, boolean notify)
	{
		fixedDomainAxisSpace = space;
		if (notify)
			fireChangeEvent();
	}

	public AxisSpace getFixedRangeAxisSpace()
	{
		return fixedRangeAxisSpace;
	}

	public void setFixedRangeAxisSpace(AxisSpace space)
	{
		setFixedRangeAxisSpace(space, true);
	}

	public void setFixedRangeAxisSpace(AxisSpace space, boolean notify)
	{
		fixedRangeAxisSpace = space;
		if (notify)
			fireChangeEvent();
	}

	public boolean isDomainPannable()
	{
		return domainPannable;
	}

	public void setDomainPannable(boolean pannable)
	{
		domainPannable = pannable;
	}

	public boolean isRangePannable()
	{
		return rangePannable;
	}

	public void setRangePannable(boolean pannable)
	{
		rangePannable = pannable;
	}

	public void panDomainAxes(double percent, PlotRenderingInfo info, Point2D source)
	{
		if (!isDomainPannable())
			return;
		int domainAxisCount = getDomainAxisCount();
		for (int i = 0; i < domainAxisCount; i++)
		{
			ValueAxis axis = getDomainAxis(i);
			if (axis == null)
				continue;
			if (axis.isInverted())
				percent = -percent;
			axis.pan(percent);
		}

	}

	public void panRangeAxes(double percent, PlotRenderingInfo info, Point2D source)
	{
		if (!isRangePannable())
			return;
		int rangeAxisCount = getRangeAxisCount();
		for (int i = 0; i < rangeAxisCount; i++)
		{
			ValueAxis axis = getRangeAxis(i);
			if (axis == null)
				continue;
			if (axis.isInverted())
				percent = -percent;
			axis.pan(percent);
		}

	}

	public void zoomDomainAxes(double factor, PlotRenderingInfo info, Point2D source)
	{
		zoomDomainAxes(factor, info, source, false);
	}

	public void zoomDomainAxes(double factor, PlotRenderingInfo info, Point2D source, boolean useAnchor)
	{
		for (int i = 0; i < domainAxes.size(); i++)
		{
			ValueAxis domainAxis = (ValueAxis)domainAxes.get(i);
			if (domainAxis == null)
				continue;
			if (useAnchor)
			{
				double sourceX = source.getX();
				if (orientation == PlotOrientation.HORIZONTAL)
					sourceX = source.getY();
				double anchorX = domainAxis.java2DToValue(sourceX, info.getDataArea(), getDomainAxisEdge());
				domainAxis.resizeRange2(factor, anchorX);
			} else
			{
				domainAxis.resizeRange(factor);
			}
		}

	}

	public void zoomDomainAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source)
	{
		for (int i = 0; i < domainAxes.size(); i++)
		{
			ValueAxis domainAxis = (ValueAxis)domainAxes.get(i);
			if (domainAxis != null)
				domainAxis.zoomRange(lowerPercent, upperPercent);
		}

	}

	public void zoomRangeAxes(double factor, PlotRenderingInfo info, Point2D source)
	{
		zoomRangeAxes(factor, info, source, false);
	}

	public void zoomRangeAxes(double factor, PlotRenderingInfo info, Point2D source, boolean useAnchor)
	{
		for (int i = 0; i < rangeAxes.size(); i++)
		{
			ValueAxis rangeAxis = (ValueAxis)rangeAxes.get(i);
			if (rangeAxis == null)
				continue;
			if (useAnchor)
			{
				double sourceY = source.getY();
				if (orientation == PlotOrientation.HORIZONTAL)
					sourceY = source.getX();
				double anchorY = rangeAxis.java2DToValue(sourceY, info.getDataArea(), getRangeAxisEdge());
				rangeAxis.resizeRange2(factor, anchorY);
			} else
			{
				rangeAxis.resizeRange(factor);
			}
		}

	}

	public void zoomRangeAxes(double lowerPercent, double upperPercent, PlotRenderingInfo info, Point2D source)
	{
		for (int i = 0; i < rangeAxes.size(); i++)
		{
			ValueAxis rangeAxis = (ValueAxis)rangeAxes.get(i);
			if (rangeAxis != null)
				rangeAxis.zoomRange(lowerPercent, upperPercent);
		}

	}

	public boolean isDomainZoomable()
	{
		return true;
	}

	public boolean isRangeZoomable()
	{
		return true;
	}

	public int getSeriesCount()
	{
		int result = 0;
		XYDataset dataset = getDataset();
		if (dataset != null)
			result = dataset.getSeriesCount();
		return result;
	}

	public LegendItemCollection getFixedLegendItems()
	{
		return fixedLegendItems;
	}

	public void setFixedLegendItems(LegendItemCollection items)
	{
		fixedLegendItems = items;
		fireChangeEvent();
	}

	public LegendItemCollection getLegendItems()
	{
		if (fixedLegendItems != null)
			return fixedLegendItems;
		LegendItemCollection result = new LegendItemCollection();
		int count = datasets.size();
		for (int datasetIndex = 0; datasetIndex < count; datasetIndex++)
		{
			XYDataset dataset = getDataset(datasetIndex);
			if (dataset == null)
				continue;
			XYItemRenderer renderer = getRenderer(datasetIndex);
			if (renderer == null)
				renderer = getRenderer(0);
			if (renderer == null)
				continue;
			int seriesCount = dataset.getSeriesCount();
			for (int i = 0; i < seriesCount; i++)
			{
				if (!renderer.isSeriesVisible(i) || !renderer.isSeriesVisibleInLegend(i))
					continue;
				org.jfree.chart.LegendItem item = renderer.getLegendItem(datasetIndex, i);
				if (item != null)
					result.add(item);
			}

		}

		return result;
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof XYPlot))
			return false;
		XYPlot that = (XYPlot)obj;
		if (weight != that.weight)
			return false;
		if (orientation != that.orientation)
			return false;
		if (!domainAxes.equals(that.domainAxes))
			return false;
		if (!domainAxisLocations.equals(that.domainAxisLocations))
			return false;
		if (rangeCrosshairLockedOnData != that.rangeCrosshairLockedOnData)
			return false;
		if (domainGridlinesVisible != that.domainGridlinesVisible)
			return false;
		if (rangeGridlinesVisible != that.rangeGridlinesVisible)
			return false;
		if (domainMinorGridlinesVisible != that.domainMinorGridlinesVisible)
			return false;
		if (rangeMinorGridlinesVisible != that.rangeMinorGridlinesVisible)
			return false;
		if (domainZeroBaselineVisible != that.domainZeroBaselineVisible)
			return false;
		if (rangeZeroBaselineVisible != that.rangeZeroBaselineVisible)
			return false;
		if (domainCrosshairVisible != that.domainCrosshairVisible)
			return false;
		if (domainCrosshairValue != that.domainCrosshairValue)
			return false;
		if (domainCrosshairLockedOnData != that.domainCrosshairLockedOnData)
			return false;
		if (rangeCrosshairVisible != that.rangeCrosshairVisible)
			return false;
		if (rangeCrosshairValue != that.rangeCrosshairValue)
			return false;
		if (!ObjectUtilities.equal(axisOffset, that.axisOffset))
			return false;
		if (!ObjectUtilities.equal(renderers, that.renderers))
			return false;
		if (!ObjectUtilities.equal(rangeAxes, that.rangeAxes))
			return false;
		if (!rangeAxisLocations.equals(that.rangeAxisLocations))
			return false;
		if (!ObjectUtilities.equal(datasetToDomainAxesMap, that.datasetToDomainAxesMap))
			return false;
		if (!ObjectUtilities.equal(datasetToRangeAxesMap, that.datasetToRangeAxesMap))
			return false;
		if (!ObjectUtilities.equal(domainGridlineStroke, that.domainGridlineStroke))
			return false;
		if (!PaintUtilities.equal(domainGridlinePaint, that.domainGridlinePaint))
			return false;
		if (!ObjectUtilities.equal(rangeGridlineStroke, that.rangeGridlineStroke))
			return false;
		if (!PaintUtilities.equal(rangeGridlinePaint, that.rangeGridlinePaint))
			return false;
		if (!ObjectUtilities.equal(domainMinorGridlineStroke, that.domainMinorGridlineStroke))
			return false;
		if (!PaintUtilities.equal(domainMinorGridlinePaint, that.domainMinorGridlinePaint))
			return false;
		if (!ObjectUtilities.equal(rangeMinorGridlineStroke, that.rangeMinorGridlineStroke))
			return false;
		if (!PaintUtilities.equal(rangeMinorGridlinePaint, that.rangeMinorGridlinePaint))
			return false;
		if (!PaintUtilities.equal(domainZeroBaselinePaint, that.domainZeroBaselinePaint))
			return false;
		if (!ObjectUtilities.equal(domainZeroBaselineStroke, that.domainZeroBaselineStroke))
			return false;
		if (!PaintUtilities.equal(rangeZeroBaselinePaint, that.rangeZeroBaselinePaint))
			return false;
		if (!ObjectUtilities.equal(rangeZeroBaselineStroke, that.rangeZeroBaselineStroke))
			return false;
		if (!ObjectUtilities.equal(domainCrosshairStroke, that.domainCrosshairStroke))
			return false;
		if (!PaintUtilities.equal(domainCrosshairPaint, that.domainCrosshairPaint))
			return false;
		if (!ObjectUtilities.equal(rangeCrosshairStroke, that.rangeCrosshairStroke))
			return false;
		if (!PaintUtilities.equal(rangeCrosshairPaint, that.rangeCrosshairPaint))
			return false;
		if (!ObjectUtilities.equal(foregroundDomainMarkers, that.foregroundDomainMarkers))
			return false;
		if (!ObjectUtilities.equal(backgroundDomainMarkers, that.backgroundDomainMarkers))
			return false;
		if (!ObjectUtilities.equal(foregroundRangeMarkers, that.foregroundRangeMarkers))
			return false;
		if (!ObjectUtilities.equal(backgroundRangeMarkers, that.backgroundRangeMarkers))
			return false;
		if (!ObjectUtilities.equal(foregroundDomainMarkers, that.foregroundDomainMarkers))
			return false;
		if (!ObjectUtilities.equal(backgroundDomainMarkers, that.backgroundDomainMarkers))
			return false;
		if (!ObjectUtilities.equal(foregroundRangeMarkers, that.foregroundRangeMarkers))
			return false;
		if (!ObjectUtilities.equal(backgroundRangeMarkers, that.backgroundRangeMarkers))
			return false;
		if (!ObjectUtilities.equal(annotations, that.annotations))
			return false;
		if (!PaintUtilities.equal(domainTickBandPaint, that.domainTickBandPaint))
			return false;
		if (!PaintUtilities.equal(rangeTickBandPaint, that.rangeTickBandPaint))
			return false;
		if (!quadrantOrigin.equals(that.quadrantOrigin))
			return false;
		for (int i = 0; i < 4; i++)
			if (!PaintUtilities.equal(quadrantPaint[i], that.quadrantPaint[i]))
				return false;

		return super.equals(obj);
	}

	public Object clone()
		throws CloneNotSupportedException
	{
		XYPlot clone = (XYPlot)super.clone();
		clone.domainAxes = (ObjectList)ObjectUtilities.clone(domainAxes);
		for (int i = 0; i < domainAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)domainAxes.get(i);
			if (axis != null)
			{
				ValueAxis clonedAxis = (ValueAxis)axis.clone();
				clone.domainAxes.set(i, clonedAxis);
				clonedAxis.setPlot(clone);
				clonedAxis.addChangeListener(clone);
			}
		}

		clone.domainAxisLocations = (ObjectList)domainAxisLocations.clone();
		clone.rangeAxes = (ObjectList)ObjectUtilities.clone(rangeAxes);
		for (int i = 0; i < rangeAxes.size(); i++)
		{
			ValueAxis axis = (ValueAxis)rangeAxes.get(i);
			if (axis != null)
			{
				ValueAxis clonedAxis = (ValueAxis)axis.clone();
				clone.rangeAxes.set(i, clonedAxis);
				clonedAxis.setPlot(clone);
				clonedAxis.addChangeListener(clone);
			}
		}

		clone.rangeAxisLocations = (ObjectList)ObjectUtilities.clone(rangeAxisLocations);
		clone.datasets = (ObjectList)ObjectUtilities.clone(datasets);
		for (int i = 0; i < clone.datasets.size(); i++)
		{
			XYDataset d = getDataset(i);
			if (d != null)
				d.addChangeListener(clone);
		}

		clone.datasetToDomainAxesMap = new TreeMap();
		clone.datasetToDomainAxesMap.putAll(datasetToDomainAxesMap);
		clone.datasetToRangeAxesMap = new TreeMap();
		clone.datasetToRangeAxesMap.putAll(datasetToRangeAxesMap);
		clone.renderers = (ObjectList)ObjectUtilities.clone(renderers);
		for (int i = 0; i < renderers.size(); i++)
		{
			XYItemRenderer renderer2 = (XYItemRenderer)renderers.get(i);
			if (renderer2 instanceof PublicCloneable)
			{
				PublicCloneable pc = (PublicCloneable)renderer2;
				clone.renderers.set(i, pc.clone());
			}
		}

		clone.foregroundDomainMarkers = (Map)ObjectUtilities.clone(foregroundDomainMarkers);
		clone.backgroundDomainMarkers = (Map)ObjectUtilities.clone(backgroundDomainMarkers);
		clone.foregroundRangeMarkers = (Map)ObjectUtilities.clone(foregroundRangeMarkers);
		clone.backgroundRangeMarkers = (Map)ObjectUtilities.clone(backgroundRangeMarkers);
		clone.annotations = (java.util.List)ObjectUtilities.deepClone(annotations);
		if (fixedDomainAxisSpace != null)
			clone.fixedDomainAxisSpace = (AxisSpace)ObjectUtilities.clone(fixedDomainAxisSpace);
		if (fixedRangeAxisSpace != null)
			clone.fixedRangeAxisSpace = (AxisSpace)ObjectUtilities.clone(fixedRangeAxisSpace);
		clone.quadrantOrigin = (Point2D)ObjectUtilities.clone(quadrantOrigin);
		clone.quadrantPaint = (Paint[])(Paint[])quadrantPaint.clone();
		return clone;
	}

	private void writeObject(ObjectOutputStream stream)
		throws IOException
	{
		stream.defaultWriteObject();
		SerialUtilities.writeStroke(domainGridlineStroke, stream);
		SerialUtilities.writePaint(domainGridlinePaint, stream);
		SerialUtilities.writeStroke(rangeGridlineStroke, stream);
		SerialUtilities.writePaint(rangeGridlinePaint, stream);
		SerialUtilities.writeStroke(domainMinorGridlineStroke, stream);
		SerialUtilities.writePaint(domainMinorGridlinePaint, stream);
		SerialUtilities.writeStroke(rangeMinorGridlineStroke, stream);
		SerialUtilities.writePaint(rangeMinorGridlinePaint, stream);
		SerialUtilities.writeStroke(rangeZeroBaselineStroke, stream);
		SerialUtilities.writePaint(rangeZeroBaselinePaint, stream);
		SerialUtilities.writeStroke(domainCrosshairStroke, stream);
		SerialUtilities.writePaint(domainCrosshairPaint, stream);
		SerialUtilities.writeStroke(rangeCrosshairStroke, stream);
		SerialUtilities.writePaint(rangeCrosshairPaint, stream);
		SerialUtilities.writePaint(domainTickBandPaint, stream);
		SerialUtilities.writePaint(rangeTickBandPaint, stream);
		SerialUtilities.writePoint2D(quadrantOrigin, stream);
		for (int i = 0; i < 4; i++)
			SerialUtilities.writePaint(quadrantPaint[i], stream);

		SerialUtilities.writeStroke(domainZeroBaselineStroke, stream);
		SerialUtilities.writePaint(domainZeroBaselinePaint, stream);
	}

	private void readObject(ObjectInputStream stream)
		throws IOException, ClassNotFoundException
	{
		stream.defaultReadObject();
		domainGridlineStroke = SerialUtilities.readStroke(stream);
		domainGridlinePaint = SerialUtilities.readPaint(stream);
		rangeGridlineStroke = SerialUtilities.readStroke(stream);
		rangeGridlinePaint = SerialUtilities.readPaint(stream);
		domainMinorGridlineStroke = SerialUtilities.readStroke(stream);
		domainMinorGridlinePaint = SerialUtilities.readPaint(stream);
		rangeMinorGridlineStroke = SerialUtilities.readStroke(stream);
		rangeMinorGridlinePaint = SerialUtilities.readPaint(stream);
		rangeZeroBaselineStroke = SerialUtilities.readStroke(stream);
		rangeZeroBaselinePaint = SerialUtilities.readPaint(stream);
		domainCrosshairStroke = SerialUtilities.readStroke(stream);
		domainCrosshairPaint = SerialUtilities.readPaint(stream);
		rangeCrosshairStroke = SerialUtilities.readStroke(stream);
		rangeCrosshairPaint = SerialUtilities.readPaint(stream);
		domainTickBandPaint = SerialUtilities.readPaint(stream);
		rangeTickBandPaint = SerialUtilities.readPaint(stream);
		quadrantOrigin = SerialUtilities.readPoint2D(stream);
		quadrantPaint = new Paint[4];
		for (int i = 0; i < 4; i++)
			quadrantPaint[i] = SerialUtilities.readPaint(stream);

		domainZeroBaselineStroke = SerialUtilities.readStroke(stream);
		domainZeroBaselinePaint = SerialUtilities.readPaint(stream);
		int domainAxisCount = domainAxes.size();
		for (int i = 0; i < domainAxisCount; i++)
		{
			Axis axis = (Axis)domainAxes.get(i);
			if (axis != null)
			{
				axis.setPlot(this);
				axis.addChangeListener(this);
			}
		}

		int rangeAxisCount = rangeAxes.size();
		for (int i = 0; i < rangeAxisCount; i++)
		{
			Axis axis = (Axis)rangeAxes.get(i);
			if (axis != null)
			{
				axis.setPlot(this);
				axis.addChangeListener(this);
			}
		}

		int datasetCount = datasets.size();
		for (int i = 0; i < datasetCount; i++)
		{
			Dataset dataset = (Dataset)datasets.get(i);
			if (dataset != null)
				dataset.addChangeListener(this);
		}

		int rendererCount = renderers.size();
		for (int i = 0; i < rendererCount; i++)
		{
			XYItemRenderer renderer = (XYItemRenderer)renderers.get(i);
			if (renderer != null)
				renderer.addChangeListener(this);
		}

	}

	static 
	{
		DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5F, 0, 2, 0.0F, new float[] {
			2.0F, 2.0F
		}, 0.0F);
		DEFAULT_GRIDLINE_PAINT = Color.lightGray;
		DEFAULT_CROSSHAIR_STROKE = DEFAULT_GRIDLINE_STROKE;
		DEFAULT_CROSSHAIR_PAINT = Color.blue;
	}
}
