package sql;

import java.sql.Connection;

public class StartConnection {
	/**
	 * 模拟线程启动 去获得连接
	 */
	private static StartConnection threadConnection;
	private IConnectionPool pool = ConnectionPoolManager.getInstance().getPool("mappool");

	public static StartConnection instance() {
		if (threadConnection == null) {
			threadConnection = new StartConnection();
		}

		return threadConnection;
	}

	public StartConnection() {
		threadConnection = this;
	}

	public Connection getConnection() {
		Connection conn = null;
		if (pool != null && pool.isActive()) {
			conn = pool.getConnection();
		}
		return conn;
	}
	
	public void close(Connection conn){
		ConnectionPoolManager.getInstance().close("mappool",conn);
	}

	public Connection getCurrentConnection() {
		Connection conn = null;
		if (pool != null && pool.isActive()) {
			conn = pool.getCurrentConnecton();
		}
		return conn;
	}
}
