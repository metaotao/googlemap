package sql;

import java.sql.Connection;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetFileName {
	private static GetFileName getFileName;
	private ArrayList<Integer> list;

	public static GetFileName instance() {
		if (getFileName == null) {
			getFileName = new GetFileName();
		}
		return getFileName;
	}

	public GetFileName() {
		getFileName = this;
	}

	public ArrayList<Integer> executeQuery(String info) {
		list = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet rs = null;
		try {
			conn = StartConnection.instance().getConnection();
			String sql = "select pro_ID,pro_city_ID,RID from pro_city_county where name='" + info + "'";

			pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt(1));
				list.add(rs.getInt(2));
				list.add(rs.getInt(3));
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
		return list;
	}
}
