/* 
 * @(#)SQLBatchStatement.java
 * Statement�� Batch ó���� �̿��ϱ� ���� ��ü
 */
package framework.db;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SQLBatchPreparedStatement extends DBStatement {
	private String _sql;
	private ConnectionManager _connMgr = null;
	private PreparedStatement _pstmt = null;
	private List<List<Object>> _paramList = new ArrayList<List<Object>>();
	private Object _caller = null;

	public static SQLBatchPreparedStatement create(String sql, ConnectionManager connMgr, Object caller) {
		return new SQLBatchPreparedStatement(sql, connMgr, caller);
	}

	private SQLBatchPreparedStatement(String sql, ConnectionManager connMgr, Object caller) {
		_sql = sql;
		_connMgr = connMgr;
		_caller = caller;
	}

	public void addBatch(Object[] where) {
		List<Object> param = new ArrayList<Object>();
		for (Object obj : where) {
			param.add(obj);
		}
		_paramList.add(param);
	}

	protected PreparedStatement getPrepareStatment() {
		if (getSQL() == null) {
			getLogger().error("Query is Null");
			return null;
		}
		try {
			if (_pstmt == null) {
				_pstmt = _connMgr.getRawConnection().prepareStatement(getSQL(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				_pstmt.setFetchSize(100);
			}
		} catch (SQLException e) {
			getLogger().error("getPrepareStatment Error!");
			throw new RuntimeException(e);
		}
		return _pstmt;
	}

	@Override
	public void close() {
		try {
			if (_pstmt != null) {
				_pstmt.close();
				_pstmt = null;
			}
			clearParamList();
		} catch (SQLException e) {
			getLogger().error("close Error!");
			throw new RuntimeException(e);
		}
	}

	public void clearParamList() {
		_paramList = new ArrayList<List<Object>>();
	}

	public int[] executeBatch() {
		if (getSQL() == null) {
			getLogger().error("Query is Null");
			return new int[] { 0 };
		}
		int[] _upCnts = null;
		try {
			PreparedStatement pstmt = getPrepareStatment();
			if (getLogger().isDebugEnabled()) {
				StringBuilder log = new StringBuilder();
				log.append("@Sql Start (BATCH P_STATEMENT) FetchSize : " + pstmt.getFetchSize() + " Caller : " + _caller.getClass().getName() + "\n");
				log.append("@Sql Command => \n" + getQueryString());
				getLogger().debug(log.toString());
			}
			for (List<Object> params : _paramList) {
				for (int i = 1, length = params.size(); i <= length; i++) {
					Object param = params.get(i - 1);
					if (param == null || "".equals(param)) {
						pstmt.setNull(i, java.sql.Types.VARCHAR);
					} else if (param instanceof String) {
						pstmt.setString(i, param.toString());
					} else if (param instanceof byte[]) {
						int size = ((byte[]) param).length;
						if (size > 0) {
							InputStream is = new ByteArrayInputStream((byte[]) param);
							pstmt.setBinaryStream(i, is, size);
						} else {
							pstmt.setBinaryStream(i, null, 0);
						}
					} else {
						pstmt.setObject(i, param);
					}
				}
				pstmt.addBatch();
			}
			_upCnts = pstmt.executeBatch();
			if (getLogger().isDebugEnabled()) {
				getLogger().debug("@Sql End (BATCH P_STATEMENT)");
			}
		} catch (SQLException e) {
			getLogger().error("executeQuery Error!");
			throw new RuntimeException(e.getMessage() + "\nSQL : " + getQueryString());
		}
		return _upCnts;
	}

	public void setSQL(String newSql) {
		close();
		_sql = newSql;
	}

	public String getSQL() {
		return _sql;
	}

	public String toString() {
		return "SQL : " + getSQL();
	}

	public String getQueryString() {
		StringBuilder buf = new StringBuilder();
		for (List<Object> param : _paramList) {
			Object value = null;
			int qMarkCount = 0;
			StringTokenizer token = new StringTokenizer(getSQL(), "?");
			while (token.hasMoreTokens()) {
				String oneChunk = token.nextToken();
				buf.append(oneChunk);
				if (param.size() > qMarkCount) {
					value = param.get(qMarkCount++);
					if (value == null || "".equals(value)) {
						value = "NULL";
					} else if (value instanceof String || value instanceof Date) {
						value = "'" + value + "'";
					}
				} else {
					if (token.hasMoreTokens()) {
						value = null;
					} else {
						value = "";
					}
				}
				buf.append("" + value);
			}
			buf.append("\n");
		}
		return buf.toString().trim();
	}
}