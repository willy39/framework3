/*
 * @(#)DataTablesUtil.java
 */
package framework.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import framework.db.RecordSet;

/**
 * DataTables �� �̿��Ͽ� ������ �� �̿��� �� �ִ� ��ƿ��Ƽ Ŭ�����̴�.
 */
public class DataTablesUtil {

	/**
	 * ������, �ܺο��� ��ü�� �ν��Ͻ�ȭ �� �� ������ ����
	 */
	private DataTablesUtil() {
	}

	/**
	 * RecordSet�� DataTables �������� ����Ѵ�.
	 * <br>
	 * ex) response�� rs�� DataTables �������� ����ϴ� ��� => DataTablesUtil.render(response, rs)
	 * @param response Ŭ���̾�Ʈ�� ������ Response ��ü
	 * @param rs DataTables �������� ��ȯ�� RecordSet ��ü
	 * @return ó���Ǽ�
	 */
	public static int render(HttpServletResponse response, RecordSet rs) {
		return _setRecordSet(response, rs);
	}

	/**
	 * RecordSet�� DataTables �������� ����Ѵ�.
	 * <br>
	 * ex) response�� rs�� DataTables �������� ����ϴ� ��� => DataTablesUtil.render(response, rs, new String[] { "col1", "col2" })
	 * @param response Ŭ���̾�Ʈ�� ������ Response ��ü
	 * @param rs DataTables �������� ��ȯ�� RecordSet ��ü
	 * @param colNames �÷��̸� �迭
	 * @return ó���Ǽ�
	 */
	public static int render(HttpServletResponse response, RecordSet rs, String[] colNames) {
		return _setRecordSet(response, rs, colNames);
	}

	/**
	 * RecordSet�� DataTables �������� ��ȯ�Ѵ�.
	 * <br>
	 * ex) rs�� DataTables �������� ��ȯ�ϴ� ��� => String json = DataTablesUtil.render(rs)
	 * @param rs DataTables �������� ��ȯ�� RecordSet ��ü
	 * @return DataTables �������� ��ȯ�� ���ڿ�
	 */
	public static String render(RecordSet rs) {
		return _format(rs);
	}

	/**
	 * RecordSet�� DataTables �������� ��ȯ�Ѵ�.
	 * <br>
	 * ex) rs�� DataTables �������� ��ȯ�ϴ� ��� => String json = DataTablesUtil.render(rs, new String[] { "col1", "col2" })
	 * @param rs DataTables �������� ��ȯ�� RecordSet ��ü
	 * @param colNames �÷��̸� �迭
	 * @return DataTables �������� ��ȯ�� ���ڿ�
	 */
	public static String render(RecordSet rs, String[] colNames) {
		return _format(rs, colNames);
	}

	/**
	 * ResultSet�� DataTables �������� ����Ѵ�. 
	 * <br>
	 * ex) response�� rs�� DataTables �������� ����ϴ� ��� => DataTablesUtil.render(response, rs)
	 * @param response Ŭ���̾�Ʈ�� ������ Response ��ü
	 * @param rs DataTables �������� ��ȯ�� ResultSet ��ü, ResultSet ��ü�� �ڵ����� close �ȴ�.
	 * @return ó���Ǽ�
	 */
	public static int render(HttpServletResponse response, ResultSet rs) {
		return _setResultSet(response, rs);
	}

	/**
	 * ResultSet�� DataTables �������� ����Ѵ�.
	 * <br>
	 * ex) response�� rs�� DataTables �������� ����ϴ� ��� => DataTablesUtil.render(response, rs, new String[] { "col1", "col2" })
	 * @param response Ŭ���̾�Ʈ�� ������ Response ��ü
	 * @param rs DataTables �������� ��ȯ�� ResultSet ��ü, ResultSet ��ü�� �ڵ����� close �ȴ�.
	 * @param colNames �÷��̸� �迭
	 * @return ó���Ǽ�
	 */
	public static int render(HttpServletResponse response, ResultSet rs, String[] colNames) {
		return _setResultSet(response, rs, colNames);
	}

	/**
	 * ResultSet�� DataTables �������� ��ȯ�Ѵ�.
	 * <br>
	 * ex) rs�� DataTables �������� ��ȯ�ϴ� ��� => String json = DataTablesUtil.render(rs)
	 * @param rs DataTables �������� ��ȯ�� ResultSet ��ü
	 * @return DataTables �������� ��ȯ�� ���ڿ�
	 */
	public static String render(ResultSet rs) {
		return _format(rs);
	}

	/**
	 * ResultSet�� DataTables �������� ��ȯ�Ѵ�. 
	 * <br>
	 * ex) rs�� DataTables �������� ��ȯ�ϴ� ��� => String json = DataTablesUtil.render(rs, new String[] { "col1", "col2" })
	 * @param rs DataTables �������� ��ȯ�� ResultSet ��ü
	 * @param colNames �÷��̸� �迭
	 * @return DataTables �������� ��ȯ�� ���ڿ�
	 */
	public static String render(ResultSet rs, String[] colNames) {
		return _format(rs, colNames);
	}

	/**
	 * List��ü�� DataTables �������� ��ȯ�Ѵ�. DataTablesUtil.format�� ����
	 * <br>
	 * ex1) mapList�� DataTables �������� ��ȯ�ϴ� ��� => String json = DataTablesUtil.render(mapList)
	 * @param mapList ��ȯ�� List��ü
	 * @return DataTables �������� ��ȯ�� ���ڿ�
	 */
	public static String render(List<Map<String, Object>> mapList) {
		return _format(mapList);
	}

	/**
	 * �ڹٽ�ũ��Ʈ�� Ư���ϰ� �νĵǴ� ���ڵ��� JSON� ����ϱ� ���� ��ȯ�Ͽ��ش�.
	 * @param str ��ȯ�� ���ڿ�
	 */
	public static String escapeJS(String str) {
		if (str == null) {
			return "";
		}
		return str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("\r\n", "\\\\n").replaceAll("\n", "\\\\n");
	}

	////////////////////////////////////////////////////////////////////////////////////////// Private �޼ҵ�

	/**
	 * RecordSet�� DataTables �������� ����Ѵ�.
	 */
	private static int _setRecordSet(HttpServletResponse response, RecordSet rs) {
		if (rs == null) {
			return 0;
		}
		PrintWriter pw;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String[] colNms = rs.getColumns();
		rs.moveRow(0);
		pw.print("{");
		int rowCount = 0;
		pw.print("\"aaData\":[");
		while (rs.nextRow()) {
			if (rowCount++ > 0) {
				pw.print(",");
			}
			pw.print(_dataTablesRowStr(rs, colNms));
		}
		pw.print("]");
		pw.print("}");
		return rowCount;
	}

	/**
	 * RecordSet�� DataTables �������� ����Ѵ�.
	 */
	private static int _setRecordSet(HttpServletResponse response, RecordSet rs, String[] colNames) {
		if (rs == null) {
			return 0;
		}
		PrintWriter pw;
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		rs.moveRow(0);
		pw.print("{");
		int rowCount = 0;
		pw.print("\"aaData\":[");
		while (rs.nextRow()) {
			if (rowCount++ > 0) {
				pw.print(",");
			}
			pw.print(_dataTablesRowStr(rs, colNames));
		}
		pw.print("]");
		pw.print("}");
		return rowCount;
	}

	/**
	 * RecordSet�� DataTables �������� ��ȯ�Ѵ�.
	 */
	private static String _format(RecordSet rs) {
		StringBuilder buffer = new StringBuilder();
		if (rs == null) {
			return null;
		}
		String[] colNms = rs.getColumns();
		rs.moveRow(0);
		buffer.append("{");
		int rowCount = 0;
		buffer.append("\"aaData\":[");
		while (rs.nextRow()) {
			if (rowCount++ > 0) {
				buffer.append(",");
			}
			buffer.append(_dataTablesRowStr(rs, colNms));
		}
		buffer.append("]");
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * RecordSet�� DataTables �������� ��ȯ�Ѵ�.
	 */
	private static String _format(RecordSet rs, String[] colNames) {
		StringBuilder buffer = new StringBuilder();
		if (rs == null) {
			return null;
		}
		rs.moveRow(0);
		buffer.append("{");
		int rowCount = 0;
		buffer.append("\"aaData\":[");
		while (rs.nextRow()) {
			if (rowCount++ > 0) {
				buffer.append(",");
			}
			buffer.append(_dataTablesRowStr(rs, colNames));
		}
		buffer.append("]");
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * ResultSet�� DataTables �������� ����Ѵ�.
	 */
	private static int _setResultSet(HttpServletResponse response, ResultSet rs) {
		if (rs == null) {
			return 0;
		}
		try {
			PrintWriter pw = response.getWriter();
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				String[] colNms = new String[count];
				pw.print("{");
				int rowCount = 0;
				pw.print("\"aaData\":[");
				while (rs.next()) {
					if (rowCount++ > 0) {
						pw.print(",");
					}
					pw.print(_dataTablesRowStr(rs, colNms));
				}
				pw.print("]");
				pw.print("}");
				return rowCount;
			} finally {
				Statement stmt = rs.getStatement();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ResultSet�� DataTables �������� ����Ѵ�.
	 */
	private static int _setResultSet(HttpServletResponse response, ResultSet rs, String[] colNames) {
		if (rs == null) {
			return 0;
		}
		try {
			PrintWriter pw = response.getWriter();
			try {
				pw.print("{");
				int rowCount = 0;
				pw.print("\"aaData\":[");
				while (rs.next()) {
					if (rowCount++ > 0) {
						pw.print(",");
					}
					pw.print(_dataTablesRowStr(rs, colNames));
				}
				pw.print("]");
				pw.print("}");
				return rowCount;
			} finally {
				Statement stmt = rs.getStatement();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ResultSet�� DataTables �������� ��ȯ�Ѵ�.
	 */
	private static String _format(ResultSet rs) {
		if (rs == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		try {
			try {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				String[] colNms = new String[count];
				int rowCount = 0;
				buffer.append("{");
				buffer.append("\"aaData\":[");
				while (rs.next()) {
					if (rowCount++ > 0) {
						buffer.append(",");
					}
					buffer.append(_dataTablesRowStr(rs, colNms));
				}
				buffer.append("]");
				buffer.append("}");
			} finally {
				Statement stmt = rs.getStatement();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return buffer.toString();
	}

	/**
	 * ResultSet�� DataTables �������� ��ȯ�Ѵ�.
	 */
	private static String _format(ResultSet rs, String[] colNames) {
		if (rs == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		try {
			try {
				int rowCount = 0;
				buffer.append("{");
				buffer.append("\"aaData\":[");
				while (rs.next()) {
					if (rowCount++ > 0) {
						buffer.append(",");
					}
					buffer.append(_dataTablesRowStr(rs, colNames));
				}
				buffer.append("]");
				buffer.append("}");
			} finally {
				Statement stmt = rs.getStatement();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return buffer.toString();
	}

	/**
	 * List��ü�� DataTables �������� ��ȯ�Ѵ�.
	 */
	private static String _format(List<Map<String, Object>> mapList) {
		if (mapList == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("\"aaData\":");
		if (mapList.size() > 0) {
			buffer.append("[");
			for (Map<String, Object> map : mapList) {
				buffer.append(_dataTablesRowStr(map));
				buffer.append(",");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			buffer.append("]");
		} else {
			buffer.append("[]");
		}
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * DataTables �� Row ���ڿ� ����
	 */
	private static String _dataTablesRowStr(Map<String, Object> map) {
		StringBuilder buffer = new StringBuilder();
		if (map.entrySet().size() > 0) {
			buffer.append("[");
			for (Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				if (value == null) {
					buffer.append("\"\"");
				} else {
					buffer.append("\"" + escapeJS(value.toString()) + "\"");
				}
				buffer.append(",");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			buffer.append("]");
		} else {
			buffer.append("[]");
		}
		return buffer.toString();
	}

	/**
	 * DataTables �� Row ���ڿ� ����
	 */
	private static String _dataTablesRowStr(RecordSet rs, String[] colNms) {
		StringBuilder buffer = new StringBuilder();
		if (colNms.length > 0) {
			buffer.append("[");
			for (int c = 0; c < colNms.length; c++) {
				Object value = rs.get(colNms[c].toUpperCase());
				if (value == null) {
					buffer.append("\"\"");
				} else {
					buffer.append("\"" + escapeJS(value.toString()) + "\"");
				}
				buffer.append(",");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			buffer.append("]");
		} else {
			buffer.append("[]");
		}
		return buffer.toString();
	}

	private static String _dataTablesRowStr(ResultSet rs, String[] colNms) {
		StringBuilder buffer = new StringBuilder();
		if (colNms.length > 0) {
			buffer.append("[");
			for (int c = 0; c < colNms.length; c++) {
				Object value;
				try {
					value = rs.getObject(colNms[c].toUpperCase());
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
				if (value == null) {
					buffer.append("\"\"");
				} else {
					buffer.append("\"" + escapeJS(value.toString()) + "\"");
				}
				buffer.append(",");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			buffer.append("]");
		} else {
			buffer.append("[]");
		}
		return buffer.toString();
	}
}