package tool;
/**
* @author tao
* version 1.0
*/
public class MapParameter{
	
	/**
	* 图片长宽，google map经纬度，级数
	*/
	public static int MAP_WIDTH=256;
	public static int MAP_HEIGHT=256;
	private int latitude;
	private int longitude;
	public static int ZOOM=16;
	
	private int rowAmount;
	private int colAmount;
	public void setRowAmount(int rowAmount){
		this.rowAmount=rowAmount;
	}

	public int getRowAmount(){
		return rowAmount;
	}


	public void setColAmount(int colAmount){
		this.colAmount=colAmount;
	}

	public int getColAmount(){
		return colAmount;
	}

	public int getLatitude(){
		return latitude;
	}

	public void setLatitude(int latitude){
		this.latitude=latitude;
	}

	public int getLongitude(){
		return longitude;
	}

	public void setLongitude(int longitude){
		this.longitude=longitude;
	}
}