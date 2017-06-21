package tool;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DiskMapping implements Runnable{
	public DiskMapping(){
	}

	public void run(){
		Process process=null;
		try{
			System.out.println("net use "+"X:"+"\\\\"+"192.168.65.67"+"\\map");
			String cmd="ipconfig";
			process=Runtime.getRuntime().exec(cmd);
			InputStream fis=process.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line=null;
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new Thread(new DiskMapping()).start();
	}
}