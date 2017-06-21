package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;

/**
 * @author tao
 * @version 1.0
 */
public class GetProInfo {

	private static GetProInfo getInfo;
	public static GetProInfo instance() {
		if (getInfo == null) {
			getInfo = new GetProInfo();
		}
		return getInfo;
	}

	public GetProInfo() {
		getInfo = this;
	}

	public ArrayList<String> executeQuery() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pre = null;
		String sql = "select distinct province from nation_division";
		ArrayList<String> list = new ArrayList<String>();
		try {
			conn = StartConnection.instance().getConnection();
			System.out.println(conn == null);
			pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
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

	public ArrayList<String> getProLatLng(String pro) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pre = null;
		String sql = "select distinct start_ci_longitude,start_ci_latitude,end_ci_longitude,end_ci_latitude from nation_division where province='"
				+ pro + "'";
		ArrayList<String> list = new ArrayList<String>();
		try {
			conn = StartConnection.instance().getConnection();
			pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pre.close();
				StartConnection.instance().close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<String> getCountyLatLng(String pro) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pre = null;
		String sql = "select province,city,county,start_co_longitude,start_co_latitude,end_co_longitude,end_co_latitude from nation_division where start_co_longitude!=0 and province='"
				+ pro + "'";
		ArrayList<String> list = new ArrayList<String>();
		try {
			conn = StartConnection.instance().getConnection();
			pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " "
						+ rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pre.close();
				StartConnection.instance().close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}