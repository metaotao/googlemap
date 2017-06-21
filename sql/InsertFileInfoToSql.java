package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertFileInfoToSql {
	private static InsertFileInfoToSql insertFileInfoToSql;
	private static String path;
	private String tablename;
	public static InsertFileInfoToSql instance() {
		if (insertFileInfoToSql == null) {
			insertFileInfoToSql = new InsertFileInfoToSql(path);
		}
		return insertFileInfoToSql;
	}

	public InsertFileInfoToSql(String path) {
		this.path=path;
		insertFileInfoToSql = this;
		tablename=getTable(path);
	}
	
	public String getTable(String path){
		String[] split=path.split("\\\\");
		String name=split[1]+"_"+split[2]+"_"+split[3];
		return name;
	}

	public void insertInfo(int pro_city_county_ID,int row_ID,double longitude,int col_ID,double latitude){
		Connection conn=null;
		PreparedStatement pre=null;
		try{
			
			conn=StartConnection.instance().getConnection();
			String sql="insert into "+tablename+"(pro_city_county_ID,row_ID,longitude,col_ID,latitude) values(?,?,?,?,?)";

			pre=conn.prepareStatement(sql);
			pre.setInt(1,pro_city_county_ID);
			pre.setInt(2,row_ID);
			pre.setDouble(3,longitude);
			pre.setInt(4,col_ID);
			pre.setDouble(5,latitude);
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
}
