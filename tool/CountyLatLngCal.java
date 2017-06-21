package tool;

/**
* @author tao
* @version 1.0
*/

public class CountyLatLngCal{

	private static double lngStartPixel;
	private static double lngEndPixel;
	private static double latStartPixel;
	private static double latEndPixel;

	public CountyLatLngCal(){
	}

	public static String calculate(double start_longitude,double start_latitude,double end_longitude,double end_latitude){

		latStartPixel=Transform.latToPixel(start_latitude,MapParameter.ZOOM);
		latEndPixel=Transform.latToPixel(end_latitude,MapParameter.ZOOM);
				
		lngStartPixel=Transform.lngToPixel(start_longitude,MapParameter.ZOOM);
		lngEndPixel=Transform.lngToPixel(end_longitude,MapParameter.ZOOM);

		int colRange=(int)((lngEndPixel-lngStartPixel)/MapParameter.MAP_WIDTH);
		int rowRange=(int)((latEndPixel-latStartPixel)/MapParameter.MAP_HEIGHT);
	//	System.out.println(colRange+"   "+rowRange);
		double sum=(double)colRange*(double)rowRange;
		return colRange+"x"+rowRange;

	}
}