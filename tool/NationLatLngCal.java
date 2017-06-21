package tool;

/**
* @author tao
* @version 1.0
*/

public class NationLatLngCal{
	private double start_longitude=71.754150;
	private double start_latitude=53.651413;
	private double end_longitude=135.101503;
	private double end_latitude=18.119651;

	private double lngStartPixel;
	private double lngEndPixel;
	private double latStartPixel;
	private double latEndPixel;

	public NationLatLngCal(){
	}

	public String calculate(){
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