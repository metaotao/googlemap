package bean;
public class MapClientBean{
	private String province;
	private String city;
	private String county;

	private double start_longitude;
	private double start_latitude;
	private double end_longitude;
	private double end_latitude;

	private int colRange;
	private int rowRange;

	public void setProvince(String province){
		this.province=province;
	}

	public String getProvince(){
		return province;
	}

	public void setCity(String city){
		this.city=city;
	}

	public String getCity(){
		return city;
	}

	public void setCounty(String county){
		this.county=county;
	}

	public String getCounty(){
		return county;
	}

	public void setStart_longitude(double start_longitude){
		this.start_longitude=start_longitude;
	}

	public double getStart_longitude(){
		return start_longitude;
	}

	public void setStart_latitude(double start_latitude){
		this.start_latitude=start_latitude;
	}

	public double getStart_latitude(){
		return start_latitude;
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

	public void setColRange(int colRange){
		this.colRange=colRange;
	}

	public int getColRange(){
		return colRange;
	}

	public void setRowRange(int rowRange){
		this.rowRange=rowRange;
	}

	public int getRowRange(){
		return rowRange;
	}
}