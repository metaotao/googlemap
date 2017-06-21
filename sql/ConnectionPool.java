package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import bean.DBBean;

public class ConnectionPool implements IConnectionPool {
	
	// ���ӳ���������
	private DBBean dbBean = new DBBean();
	// ���ӳػ״̬
	private boolean isActive = false;
	// ��¼�����ܵ�������
	private int countActive = 0;
	// �ռ�����
	private List<Connection> freeConnection = new Vector<Connection>();
	// �����
	private List<Connection> activeConnection = new Vector<Connection>();

	// ���̺߳����Ӱ󶨣���֤������ͬһִ��
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

	public ConnectionPool(DBBean dbBean) {
		super();
		this.dbBean = dbBean;
		init();
		checkPool();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		try {
			Class.forName(dbBean.getDrivername());
			
			for (int i = 0; i < dbBean.getInitConnections(); i++) {
				Connection conn = newConnection();
				// ��ʼ����С������
				if (conn != null) {
					freeConnection.add(conn);
					countActive++;
				}

			}
			isActive = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ��õ�ǰ����
	public Connection getCurrentConnecton() {
		// TODO Auto-generated method stub
		// Ĭ�ϴ��߳�����ȡ
		Connection conn = threadLocal.get();
		if (!isValid(conn)) {
			conn = getConnection();
		}

		return conn;
	}

	// �������
	@Override
	public synchronized Connection getConnection() {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			// �ж��Ƿ񳬹��������������
			if (countActive < dbBean.getMaxActiveConnections()) {
				if (freeConnection.size() > 0) {
					conn = freeConnection.get(0);
					if (conn != null) {
						threadLocal.set(conn);
					}
					freeConnection.remove(0);
				} else {
					conn = newConnection();
				}
			} else {
				// ����������� ֱ�����»������
				wait(dbBean.getConnTimeOut());
				conn = getConnection();
			}

			if (isValid(conn)) {
				activeConnection.add(conn);
				countActive++;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// ���������
	private synchronized Connection newConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		if (dbBean != null) {
			Class.forName(dbBean.getDrivername());
			conn = DriverManager.getConnection(dbBean.getUrl(), dbBean.getUsername(), dbBean.getPassword());
		}
		return conn;
	}

	// �ͷ�����
	public synchronized void releaseConn(Connection conn) throws SQLException {
		if (isValid(conn) && !(freeConnection.size() > dbBean.getMaxConnections())) {
			freeConnection.add(conn);
			activeConnection.remove(conn);
			countActive--;
			threadLocal.remove();
			// �������������ȴ����߳�
			notifyAll();
		}
	}

	// �ж������Ƿ����
	private boolean isValid(Connection conn) {
		try {
			if (conn == null || conn.isClosed()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	// �������ӳ�
	public synchronized void destroy() {
		for (Connection conn : freeConnection) {
			try {
				if (isValid(conn)) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		for (Connection conn : activeConnection) {
			try {
				if (isValid(conn)) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		isActive = false;
		countActive = 0;
	}

	// ���ӳ�״̬
	@Override
	public boolean isActive() {
		return isActive;
	}

	// ��ʱ������ӳ����
	@Override
	public void checkPool() {
		if (dbBean.isCheckPool()) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					// 1.���߳����������״̬
					// 2.���ӳ���С ���������
					System.out.println("�����ӳ���������" + freeConnection.size());
					System.out.println("�����������" + activeConnection.size());
					System.out.println("�ܵ���������" + countActive);
				}
			}, dbBean.getLazyCheck(), dbBean.getPeriodCheck());
		}
	}

}
