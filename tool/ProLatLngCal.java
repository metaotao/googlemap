package tool;

import java.util.ArrayList;
/**
* @author tao
* @version 1.0
*/

public class ProLatLngCal{

	private double start_longitude;
	private double start_latitude;
	private double end_longitude;
	private double end_latitude;

	private double lngStartPixel;
	private double lngEndPixel;
	private double latStartPixel;
	private double latEndPixel;

	public ProLatLngCal(){
	}

	public String calculate(ArrayList<String> list){
		
		String[] split=list.get(0).split(" ");
		start_longitude=Double.parseDouble(split[0]);
		start_latitude=Double.parseDouble(split[1]);
		end_longitude=Double.parseDouble(split[2]);
		end_latitude=Double.parseDouble(split[3]);

		latStartPixel=Transform.latToPixel(start_latitude,MapParameter.ZOOM);
		latEndPixel=Transform.latToPixel(end_latitude,MapParameter.ZOOM);
				
		lngStartPixel=Transform.lngToPixel(start_longitude,MapParameter.ZOOM);
		lngEndPixel=Transform.lngToPixel(end_longitude,MapParameter.ZOOM);

		int colRange=(int)((lngEndPixel-lngStartPixel)/MapParameter.MAP_WIDTH);
		int rowRange=(int)((latEndPixel-latStartPixel)/MapParameter.MAP_HEIGHT);
		System.out.println(colRange+"   "+rowRange);
		double sum=(double)colRange*(double)rowRange;
		return colRange+" x "+rowRange;

	}
}