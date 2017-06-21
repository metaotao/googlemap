package sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
	// �������
	public Connection getConnection();

	// ��õ�ǰ����
	public Connection getCurrentConnecton();

	// ��������
	public void releaseConn(Connection conn) throws SQLException;

	// �������
	public void destroy();

	// ���ӳ��ǻ״̬
	public boolean isActive();

	// ��ʱ����������ӳ�
	public void checkPool();

}
