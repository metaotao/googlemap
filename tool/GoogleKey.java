package tool;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GoogleKey{

	public static ArrayList<String> load1(){
		ArrayList<String> APIList1=new ArrayList<String>();
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File("file\\googleµÿÕº√‹‘ø1.txt")));
			String str=null;
			while((str=reader.readLine())!=null){
				APIList1.add(str);
				//System.out.println(str);
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return APIList1;
	}

	public static ArrayList<String> load2(){
		ArrayList<String> APIList2=new ArrayList<String>();
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File("file\\googleµÿÕº√‹‘ø2.txt")));
			String str=null;
			while((str=reader.readLine())!=null){
				APIList2.add(str);
				
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return APIList2;
	}

	public static ArrayList<String> load3(){
		ArrayList<String> APIList3=new ArrayList<String>();
		try{
			BufferedReader reader=new BufferedReader(new FileReader(new File("file\\googleµÿÕº√‹‘ø3.txt")));
			String str=null;
			while((str=reader.readLine())!=null){
				APIList3.add(str);
				
			}			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return APIList3;
	}

	public static String getKey(ArrayList<String> list){
		return list.remove(0);
	}

	public static boolean isEmpty(ArrayList<String> list){
		if(list.size()==0){
			return false;
		}
		return true;
	}
}