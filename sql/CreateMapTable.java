package sql;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateMapTable {
	private static CreateMapTable createMapTable;

	public static CreateMapTable instance() {
		if (createMapTable == null) {
			createMapTable = new CreateMapTable();
		}
		return createMapTable;
	}

	public CreateMapTable() {
		createMapTable = this;
	}

	public void createTable(int pro_ID, int pro_city_ID, int pro_city_county_ID) {
		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		try {
			conn = StartConnection.instance().getConnection();
			String tablename = pro_ID + "_" + pro_city_ID + "_" + pro_city_county_ID;
			String sql = "create table " + tablename
					+ "(pro_city_county_ID int,row_ID int,longitude double,col_ID int,latitude double)";
			String checkTable = "show tables like \"" + tablename + "\"";
			pre = conn.prepareStatement(sql);

			rs = pre.executeQuery(checkTable);
			if (rs.next()) {
				System.out.println("table exist!");
			} else {
				if (pre.executeUpdate() == 0) {
					System.out.println("create table sueccess!");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pre.close();
				StartConnection.instance().close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}