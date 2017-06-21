package tool;
/**
* @author tao
* @version 1.0
*/

public class Transform{
	/**
	* ���ȵ�����X
	*/
	public static double lngToPixel(double longitude,int ZOOM){
		return (longitude+180)*(256L<<ZOOM)/360;
	}
	/**
	* ����X������
	*/
	public static double pixelToLng(double pixelX,int ZOOM){
		return pixelX*360/(256L<<ZOOM)-180;
	}
	/**
	* γ�ȵ�����Y
	*/
	public static double latToPixel(double latitude,int ZOOM){
		double siny=Math.sin(latitude*Math.PI/180);
		double y=Math.log((1+siny)/(1-siny));
		return (128<<ZOOM)*(1-y/(2*Math.PI));
	}
	/**
	* ����Y��γ��
	*/
	public static double pixelToLat(double pixelY,int ZOOM){
		double y=2*Math.PI*(1-pixelY/(128<<ZOOM));
		double z=Math.pow(Math.E,y);
		double siny=(z-1)/(z+1);
		return Math.asin(siny)*180/Math.PI;
	}
	/**
	* ÿ�δӶϵ��ĵط���ʼ����,��ʱͼƬ�������� γ������
	*/
	public static double getFirstLonPixel(double longitude,int ZOOM,int row){
		return lngToPixel(longitude,ZOOM)+MapParameter.MAP_WIDTH*row;
	}

	public static double getFirstLatPixel(double latitude,int ZOOM,int col){
		return latToPixel(latitude,ZOOM)+MapParameter.MAP_HEIGHT*col;
	}

	/**
	* ��ȡ�ϵ��ط�ͼƬ�ľ�γ��
	*/

	public static double getFirstLon(double longitude,int ZOOM,int row){
		return pixelToLng(getFirstLonPixel(longitude,ZOOM,row),ZOOM);
	}

	public static double getFirstLat(double latitude,int ZOOM,int col){
		return pixelToLat(getFirstLatPixel(latitude,ZOOM,col),ZOOM);
	}
	public static void main(String[] args) {
		double l1=lngToPixel(-180,20);
		double l2=lngToPixel(180,20);
		double x=(l1-l2)/256;
		double l3=latToPixel(-85,20);
		double l4=latToPixel(85,20);
		double y=(l4-l3)/256;
		System.out.println(x+"   "+y);
		System.out.println("pixel(0, 0)= " + pixelToLng(0.0, 4) + ", " + pixelToLat(0.0, 4));
		System.out.println("pixel(256, 0)= " + pixelToLng(256, 4) + ", " + pixelToLat(0, 4));
		
	//	System.out.println("Coordinates(0, 0): " + getFirstLon(29.7478301,15, 5) + ", " + getFirstLat(118.3672228491,15,5));
	}

}