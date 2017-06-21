package sql;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
/**
* @author tao
* @version 1.0
*/

public class InsertSql{
	private ArrayList<String> fileList=new ArrayList<String>();
	private ArrayList<String> list=new ArrayList<String>();
	private ArrayList<String> imageList=new ArrayList<String>();
	private static InsertSql insertSql;

	public static InsertSql instance(){
		if(insertSql==null){
			insertSql=new InsertSql();
		}
		return insertSql;
	}

	public InsertSql(){
		insertSql=this;
		insert();
	}
	
	public void getFileOnePath(){
		File[] files=new File("F:\\map").listFiles();
		for(File file:files){
			fileList.add(file.getAbsolutePath());			
		}
	}

	public void getFile(){
		getFileOnePath();
		for(int i=0;i<fileList.size();i++){
			File[] files=new File(fileList.get(i)).listFiles();
			for(File file:files){
				list.add(file.getAbsolutePath());
			}
		}
		for(int j=0;j<list.size();j++){
			File[] files=new File(list.get(j)).listFiles();
			for(File file:files){
				imageList.add(file.getAbsolutePath());
			}
		}
	}

	public void insert(){
		getFile();
		for(int i=0;i<imageList.size();i++){
			String str=imageList.get(i);
			String[] split=str.split("\\\\");
			System.out.println(str);
			insertInfo(split[2],split[3],split[4]);
		}
	}
			

	public void insertInfo(String province,String city,String area){
		Connection conn=null;
		
		PreparedStatement pre=null;
		try{
			conn=StartConnection.instance().getConnection();
			String sql="insert into image_info(province,city,area) values(?,?,?)";
			pre=conn.prepareStatement(sql);
			pre.setString(1,province);
			pre.setString(2,city);
			pre.setString(3,area);
			pre.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally {
			try{
				pre.close();
				StartConnection.instance().close(conn);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		new InsertSql();
	}
}