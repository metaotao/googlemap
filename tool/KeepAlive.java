package tool;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KeepAlive implements Serializable{    
    /**
	 * ���Ǹ÷��� 
     */  
    public String toString() {  
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());  
    }  
  
}