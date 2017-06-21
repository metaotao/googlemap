package tool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sql.StartConnection;

public class InsertInfo{
	public InsertInfo(){
		/*ArrayList<String> list=selectInfo("select distinct city from nation_division");
		String insertSql="insert into pro_city(name) values(?)";
		for(int i=0;i<list.size();i++){
			insertPro(insertSql,list.get(i));
		}*/
	}

	public ArrayList<String> selectInfo(String selectsql){
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		//String sql="select distinct province from nation_division";
		ArrayList<String> list=new ArrayList<String>();
		try{
			conn=conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement(selectsql);
			rs=pre.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
			}
			
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				pre.close();
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}

	public void insertPro(String str){
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		try{
			conn=conn=StartConnection.instance().getConnection();
			//String sql="insert into pro(name) values(?)"
			pre=conn.prepareStatement("insert into pro_city_county(name) values(?)");
			pre.setString(1,str);					
			pre.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				pre.close();
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void insertProID(int id,String name,String sql){
		
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		try{
			conn=conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement(sql);
			pre.setInt(1,id);					
			pre.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				pre.close();
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public int selectProID(String sql){
		
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		//String sql="select distinct province from nation_division";
		int ID=0;
		try{
			conn=conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement(sql);
			rs=pre.executeQuery();
			while(rs.next()){
				ID=rs.getInt(1);
			}
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				pre.close();
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return ID;
	}

	public void getResult(){
		ArrayList<String> list=selectInfo("select distinct province from nation_division");
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			ArrayList<String> cityList=selectInfo("select distinct city from nation_division where province='"+list.get(i)+"'");
			int ID=selectProID("select RID from pro where name='"+list.get(i)+"'");
			for(int j=0;j<cityList.size();j++){
				insertProID(ID,cityList.get(j),"update pro_city set pro_ID =(?) where name='"+cityList.get(j)+"'");
			}
		}
	}

	public void insertCountyInfo(String name){
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		//String sql="select distinct province from nation_division";
		
		try{
			conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement("insert into pro_city_county(name) values(?)");
			pre.setString(1,name);					
			pre.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			try{
				pre.close();
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void insertCounty(){

		for(int i=1;i<=307;i++){
			ArrayList<String> proList=selectInfo("select name from pro_city where RID="+i);
		
			int ID=selectProID("select RID from pro_city where name='"+proList.get(0)+"'");
			ArrayList<String> countyList=selectInfo("select county from nation_division where city='"+proList.get(0)+"'");
			for(int j=0;j<countyList.size();j++){
				insertPro(countyList.get(j));				
			}
			for(int j=0;j<countyList.size();j++){
				insertProID(ID,countyList.get(j),"update pro_city_county set pro_city_ID =(?) where name='"+countyList.get(j)+"'");
			}
			proList.clear();
			countyList.clear();
		}
	
	}

	public static void main(String[] args){
		new InsertInfo().insertCounty();
	}
}