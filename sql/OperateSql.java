package sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
* @author tao
* @version 1.0
*/

public class OperateSql{
	private static OperateSql operateSql;
	private Connection conn;

	public static OperateSql instance(){
		if(operateSql==null){
			operateSql=new OperateSql();
		}
		return operateSql;
	}

	public OperateSql(){
		operateSql=this;
	}

	public ArrayList<String> executeQuery(String sql){
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement pre=null;
		ArrayList<String> list=new ArrayList<String>();
		try{
			conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement(sql);
			rs=pre.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
				list.add(rs.getString(2));
				list.add(rs.getString(3));
				list.add(rs.getString(4));
				list.add(rs.getString(5));
				list.add(rs.getString(6));
				list.add(rs.getString(7));
				
			}			
			
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
		return list;
	}

	public void executeUpdate(String sql){
		Connection conn=null;
		PreparedStatement pre=null;
		try{
			conn=StartConnection.instance().getConnection();
			pre=conn.prepareStatement(sql);
			pre.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
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