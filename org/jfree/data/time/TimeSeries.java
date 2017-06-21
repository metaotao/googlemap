// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TimeSeries.java

package org.jfree.data.time;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.jfree.data.general.Series;
import org.jfree.data.general.SeriesException;
import org.jfree.util.ObjectUtilities;

// Referenced classes of package org.jfree.data.time:
//			TimeSeriesDataItem, RegularTimePeriod

public class TimeSeries extends Series
	implements Cloneable, Serializable
{

	private static final long serialVersionUID = 0xba27555b8cdb41f8L;
	protected static final String DEFAULT_DOMAIN_DESCRIPTION = "Time";
	protected static final String DEFAULT_RANGE_DESCRIPTION = "Value";
	private String domain;
	private String range;
	protected Class timePeriodClass;
	protected List data;
	private int maximumItemCount;
	private long maximumItemAge;

	public TimeSeries(Comparable name)
	{
		this(name, "Time", "Value");
	}

	public TimeSeries(Comparable name, String domain, String range)
	{
		super(name);
		this.domain = domain;
		this.range = range;
		timePeriodClass = null;
		data = new ArrayList();
		maximumItemCount = 0x7fffffff;
		maximumItemAge = 0x7fffffffffffffffL;
	}

	public String getDomainDescription()
	{
		return domain;
	}

	public void setDomainDescription(String description)
	{
		String old = domain;
		domain = description;
		firePropertyChange("Domain", old, description);
	}

	public String getRangeDescription()
	{
		return range;
	}

	public void setRangeDescription(String description)
	{
		String old = range;
		range = description;
		firePropertyChange("Range", old, description);
	}

	public int getItemCount()
	{
		return data.size();
	}

	public List getItems()
	{
		return Collections.unmodifiableList(data);
	}

	public int getMaximumItemCount()
	{
		return maximumItemCount;
	}

	public void setMaximumItemCount(int maximum)
	{
		if (maximum < 0)
			throw new IllegalArgumentException("Negative 'maximum' argument.");
		maximumItemCount = maximum;
		int count = data.size();
		if (count > maximum)
			delete(0, count - maximum - 1);
	}

	public long getMaximumItemAge()
	{
		return maximumItemAge;
	}

	public void setMaximumItemAge(long periods)
	{
		if (periods < 0L)
		{
			throw new IllegalArgumentException("Negative 'periods' argument.");
		} else
		{
			maximumItemAge = periods;
			removeAgedItems(true);
			return;
		}
	}

	public Class getTimePeriodClass()
	{
		return timePeriodClass;
	}

	public TimeSeriesDataItem getDataItem(int index)
	{
		return (TimeSeriesDataItem)data.get(index);
	}

	public TimeSeriesDataItem getDataItem(RegularTimePeriod period)
	{
		int index = getIndex(period);
		if (index >= 0)
			return (TimeSeriesDataItem)data.get(index);
		else
			return null;
	}

	public RegularTimePeriod getTimePeriod(int index)
	{
		return getDataItem(index).getPeriod();
	}

	public RegularTimePeriod getNextTimePeriod()
	{
		RegularTimePeriod last = getTimePeriod(getItemCount() - 1);
		return last.next();
	}

	public Collection getTimePeriods()
	{
		Collection result = new ArrayList();
		for (int i = 0; i < getItemCount(); i++)
			result.add(getTimePeriod(i));

		return result;
	}

	public Collection getTimePeriodsUniqueToOtherSeries(TimeSeries series)
	{
		Collection result = new ArrayList();
		for (int i = 0; i < series.getItemCount(); i++)
		{
			RegularTimePeriod period = series.getTimePeriod(i);
			int index = getIndex(period);
			if (index < 0)
				result.add(period);
		}

		return result;
	}

	public int getIndex(RegularTimePeriod period)
	{
		if (period == null)
		{
			throw new IllegalArgumentException("Null 'period' argument.");
		} else
		{
			TimeSeriesDataItem dummy = new TimeSeriesDataItem(period, -2147483648D);
			return Collections.binarySearch(data, dummy);
		}
	}

	public Number getValue(int index)
	{
		return getDataItem(index).getValue();
	}

	public Number getValue(RegularTimePeriod period)
	{
		int index = getIndex(period);
		if (index >= 0)
			return getValue(index);
		else
			return null;
	}

	public void add(TimeSeriesDataItem item)
	{
		add(item, true);
	}

	public void add(TimeSeriesDataItem item, boolean notify)
	{
		if (item == null)
			throw new IllegalArgumentException("Null 'item' argument.");
		Class c = item.getPeriod().getClass();
		if (timePeriodClass == null)
			timePeriodClass = c;
		else
		if (!timePeriodClass.equals(c))
		{
			StringBuffer b = new StringBuffer();
			b.append("You are trying to add data where the time period class ");
			b.append("is ");
			b.append(item.getPeriod().getClass().getName());
			b.append(", but the TimeSeries is expecting an instance of ");
			b.append(timePeriodClass.getName());
			b.append(".");
			throw new SeriesException(b.toString());
		}
		boolean added = false;
		int count = getItemCount();
		if (count == 0)
		{
			data.add(item);
			added = true;
		} else
		{
			RegularTimePeriod last = getTimePeriod(getItemCount() - 1);
			if (item.getPeriod().compareTo(last) > 0)
			{
				data.add(item);
				added = true;
			} else
			{
				int index = Collections.binarySearch(data, item);
				if (index < 0)
				{
					data.add(-index - 1, item);
					added = true;
				} else
				{
					StringBuffer b = new StringBuffer();
					b.append("You are attempting to add an observation for ");
					b.append("the time period ");
					b.append(item.getPeriod().toString());
					b.append(" but the series already contains an observation");
					b.append(" for that time period. Duplicates are not ");
					b.append("permitted.  Try using the addOrUpdate() method.");
					throw new SeriesException(b.toString());
				}
			}
		}
		if (added)
		{
			if (getItemCount() > maximumItemCount)
				data.remove(0);
			removeAgedItems(false);
			if (notify)
				fireSeriesChanged();
		}
	}

	public void add(RegularTimePeriod period, double value)
	{
		add(period, value, true);
	}

	public void add(RegularTimePeriod period, double value, boolean notify)
	{
		TimeSeriesDataItem item = new TimeSeriesDataItem(period, value);
		add(item, notify);
	}

	public void add(RegularTimePeriod period, Number value)
	{
		add(period, value, true);
	}

	public void add(RegularTimePeriod period, Number value, boolean notify)
	{
		TimeSeriesDataItem item = new TimeSeriesDataItem(period, value);
		add(item, notify);
	}

	public void update(RegularTimePeriod period, Number value)
	{
		TimeSeriesDataItem temp = new TimeSeriesDataItem(period, value);
		int index = Collections.binarySearch(data, temp);
		if (index >= 0)
		{
			TimeSeriesDataItem pair = (TimeSeriesDataItem)data.get(index);
			pair.setValue(value);
			fireSeriesChanged();
		} else
		{
			throw new SeriesException("There is no existing value for the specified 'period'.");
		}
	}

	public void update(int index, Number value)
	{
		TimeSeriesDataItem item = getDataItem(index);
		item.setValue(value);
		fireSeriesChanged();
	}

	public TimeSeries addAndOrUpdate(TimeSeries series)
	{
		TimeSeries overwritten = new TimeSeries("Overwritten values from: " + getKey());
		for (int i = 0; i < series.getItemCount(); i++)
		{
			TimeSeriesDataItem item = series.getDataItem(i);
			TimeSeriesDataItem oldItem = addOrUpdate(item.getPeriod(), item.getValue());
			if (oldItem != null)
				overwritten.add(oldItem);
		}

		return overwritten;
	}

	public TimeSeriesDataItem addOrUpdate(RegularTimePeriod period, double value)
	{
		return addOrUpdate(period, ((Number) (new Double(value))));
	}

	public TimeSeriesDataItem addOrUpdate(RegularTimePeriod period, Number value)
	{
		if (period == null)
			throw new IllegalArgumentException("Null 'period' argument.");
		TimeSeriesDataItem overwritten = null;
		TimeSeriesDataItem key = new TimeSeriesDataItem(period, value);
		int index = Collections.binarySearch(data, key);
		if (index >= 0)
		{
			TimeSeriesDataItem existing = (TimeSeriesDataItem)data.get(index);
			overwritten = (TimeSeriesDataItem)existing.clone();
			existing.setValue(value);
			removeAgedItems(false);
			fireSeriesChanged();
		} else
		{
			data.add(-index - 1, new TimeSeriesDataItem(period, value));
			timePeriodClass = period.getClass();
			if (getItemCount() > maximumItemCount)
			{
				data.remove(0);
				if (data.isEmpty())
					timePeriodClass = null;
			}
			removeAgedItems(false);
			fireSeriesChanged();
		}
		return overwritten;
	}

	public void removeAgedItems(boolean notify)
	{
		if (getItemCount() > 1)
		{
			long latest = getTimePeriod(getItemCount() - 1).getSerialIndex();
			boolean removed;
			for (removed = false; latest - getTimePeriod(0).getSerialIndex() > maximumItemAge; removed = true)
				data.remove(0);

			if (removed && notify)
				fireSeriesChanged();
		}
	}

	public void removeAgedItems(long latest, boolean notify)
	{
		if (data.isEmpty())
			return;
		long index = 0x7fffffffffffffffL;
		try
		{
			Method m = (org.jfree.data.time.RegularTimePeriod.class).getDeclaredMethod("createInstance", new Class[] {
				java.lang.Class.class, java.util.Date.class, java.util.TimeZone.class
			});
			RegularTimePeriod newest = (RegularTimePeriod)m.invoke(timePeriodClass, new Object[] {
				timePeriodClass, new Date(latest), TimeZone.getDefault()
			});
			index = newest.getSerialIndex();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		boolean removed;
		for (removed = false; getItemCount() > 0 && index - getTimePeriod(0).getSerialIndex() > maximumItemAge; removed = true)
			data.remove(0);

		if (removed && notify)
			fireSeriesChanged();
	}

	public void clear()
	{
		if (data.size() > 0)
		{
			data.clear();
			timePeriodClass = null;
			fireSeriesChanged();
		}
	}

	public void delete(RegularTimePeriod period)
	{
		int index = getIndex(period);
		if (index >= 0)
		{
			data.remove(index);
			if (data.isEmpty())
				timePeriodClass = null;
			fireSeriesChanged();
		}
	}

	public void delete(int start, int end)
	{
		if (end < start)
			throw new IllegalArgumentException("Requires start <= end.");
		for (int i = 0; i <= end - start; i++)
			data.remove(start);

		if (data.isEmpty())
			timePeriodClass = null;
		fireSeriesChanged();
	}

	public Object clone()
		throws CloneNotSupportedException
	{
		TimeSeries clone = (TimeSeries)super.clone();
		clone.data = (List)ObjectUtilities.deepClone(data);
		return clone;
	}

	public TimeSeries createCopy(int start, int end)
		throws CloneNotSupportedException
	{
		if (start < 0)
			throw new IllegalArgumentException("Requires start >= 0.");
		if (end < start)
			throw new IllegalArgumentException("Requires start <= end.");
		TimeSeries copy = (TimeSeries)super.clone();
		copy.data = new ArrayList();
		if (data.size() > 0)
		{
			for (int index = start; index <= end; index++)
			{
				TimeSeriesDataItem item = (TimeSeriesDataItem)data.get(index);
				TimeSeriesDataItem clone = (TimeSeriesDataItem)item.clone();
				try
				{
					copy.add(clone);
				}
				catch (SeriesException e)
				{
					e.printStackTrace();
				}
			}

		}
		return copy;
	}

	public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
		throws CloneNotSupportedException
	{
		if (start == null)
			throw new IllegalArgumentException("Null 'start' argument.");
		if (end == null)
			throw new IllegalArgumentException("Null 'end' argument.");
		if (start.compareTo(end) > 0)
			throw new IllegalArgumentException("Requires start on or before end.");
		boolean emptyRange = false;
		int startIndex = getIndex(start);
		if (startIndex < 0)
		{
			startIndex = -(startIndex + 1);
			if (startIndex == data.size())
				emptyRange = true;
		}
		int endIndex = getIndex(end);
		if (endIndex < 0)
		{
			endIndex = -(endIndex + 1);
			endIndex--;
		}
		if (endIndex < 0 || endIndex < startIndex)
			emptyRange = true;
		if (emptyRange)
		{
			TimeSeries copy = (TimeSeries)super.clone();
			copy.data = new ArrayList();
			return copy;
		} else
		{
			return createCopy(startIndex, endIndex);
		}
	}

	public boolean equals(Object object)
	{
		if (object == this)
			return true;
		if (!(object instanceof TimeSeries))
			return false;
		TimeSeries that = (TimeSeries)object;
		if (!ObjectUtilities.equal(getDomainDescription(), that.getDomainDescription()))
			return false;
		if (!ObjectUtilities.equal(getRangeDescription(), that.getRangeDescription()))
			return false;
		if (!ObjectUtilities.equal(timePeriodClass, that.timePeriodClass))
			return false;
		if (getMaximumItemAge() != that.getMaximumItemAge())
			return false;
		if (getMaximumItemCount() != that.getMaximumItemCount())
			return false;
		int count = getItemCount();
		if (count != that.getItemCount())
			return false;
		for (int i = 0; i < count; i++)
			if (!getDataItem(i).equals(that.getDataItem(i)))
				return false;

		return super.equals(object);
	}

	public int hashCode()
	{
		int result = super.hashCode();
		result = 29 * result + (domain == null ? 0 : domain.hashCode());
		result = 29 * result + (range == null ? 0 : range.hashCode());
		result = 29 * result + (timePeriodClass == null ? 0 : timePeriodClass.hashCode());
		int count = getItemCount();
		if (count > 0)
		{
			TimeSeriesDataItem item = getDataItem(0);
			result = 29 * result + item.hashCode();
		}
		if (count > 1)
		{
			TimeSeriesDataItem item = getDataItem(count - 1);
			result = 29 * result + item.hashCode();
		}
		if (count > 2)
		{
			TimeSeriesDataItem item = getDataItem(count / 2);
			result = 29 * result + item.hashCode();
		}
		result = 29 * result + maximumItemCount;
		result = 29 * result + (int)maximumItemAge;
		return result;
	}

	/**
	 * @deprecated Method TimeSeries is deprecated
	 */

	public TimeSeries(Comparable name, Class timePeriodClass)
	{
		this(name, "Time", "Value", timePeriodClass);
	}

	/**
	 * @deprecated Method TimeSeries is deprecated
	 */

	public TimeSeries(Comparable name, String domain, String range, Class timePeriodClass)
	{
		super(name);
		this.domain = domain;
		this.range = range;
		this.timePeriodClass = timePeriodClass;
		data = new ArrayList();
		maximumItemCount = 0x7fffffff;
		maximumItemAge = 0x7fffffffffffffffL;
	}
}
