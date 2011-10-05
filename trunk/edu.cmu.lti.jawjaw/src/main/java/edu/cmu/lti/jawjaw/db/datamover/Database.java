/*
 * Copyright 2009 Carnegie Mellon University
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.cmu.lti.jawjaw.db.datamover;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Database class is used to provide all of the low-level
 * JDBC services for the Datamover. Database specific 
 * implementations should be handled in derived classes,
 * for example the MySQL class.
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 *
 */
public abstract class Database {
	/*
	 * The database connection
	 */
	protected Connection connection;

  
    /**
	 * Abstrct method to process a database type. Sometimes database types are
	 * not reported exactly as they need to be for proper syntax. This method
	 * corrects the database type and size.
	 * 
	 * @param type
	 *            The type reported
	 * @param i
	 *            The size of this column
	 * @return The properly formatted type, for this database
	 */
	public abstract String processType(String type, int i);

  
    /**
	 * Open a connection to the database.
	 * 
	 * @param driver
	 *            The database driver to use.
	 * @param url
	 *            The datbase connection URL to use.
	 * @throws DatabaseException
	 *             Thrown if an error occurs while connecting.
	 */
	public void connect(String driver, String url) throws DatabaseException {
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url);
		} catch (InstantiationException e) {
			throw new DatabaseException(e);
		} catch (IllegalAccessException e) {
			throw new DatabaseException(e);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException(e);
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
  
  /**
	 * Called to close the database.
	 * 
	 * @throws DatabaseException
	 *             Thrown if the connection cannot be closed.
	 */
	public void close() throws DatabaseException {
		try {
			synchronized ( connection ) {
				if (connection!=null) connection.close();
			}
		} catch (SQLException e) {
			throw (new DatabaseException(e));
		}
	}

	/**
	 * Check to see if the specified type is numeric.
	 * 
	 * @param type
	 *            The type to check.
	 * @return Returns true if the type is numeric.
	 */
	public boolean isNumeric(int type) {
		if (type == java.sql.Types.BIGINT || type == java.sql.Types.DECIMAL
				|| type == java.sql.Types.DOUBLE
				|| type == java.sql.Types.FLOAT
				|| type == java.sql.Types.INTEGER
				|| type == java.sql.Types.NUMERIC
				|| type == java.sql.Types.SMALLINT
				|| type == java.sql.Types.TINYINT)
			return true;
		else
			return false;

	}

	/**
	 * Generate the DROP statement for a table.
	 * 
	 * @param table
	 *            The name of the table to drop.
	 * @return The SQL to drop a table.
	 */
	public String generateDrop(String table) {
		StringBuffer result = new StringBuffer();
		result.append("DROP TABLE ");
		result.append(table);
		result.append(";\n");
		return result.toString();
	}

	/**
	 * Generate the create statement to create the specified table.
	 * 
	 * @param table
	 *            The table to generate a create statement for.
	 * @return The create table statement.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public String generateCreate(String table) throws DatabaseException {
		StringBuffer result = new StringBuffer();

		ResultSetMetaData md = null;
		Statement stmt = null;
		ResultSet rs = null;
		synchronized ( connection ) {
			try {
				StringBuffer sql = new StringBuffer();
	
				sql.append("SELECT * FROM ");
				sql.append(table);
				
				stmt = connection.createStatement();
				rs = stmt.executeQuery(sql.toString());
				md = rs.getMetaData();
	
				result.append("CREATE TABLE ");
				result.append(table);
				result.append(" ( ");
	
				for (int i = 1; i <= md.getColumnCount(); i++) {
					if (i != 1)
						result.append(',');
					result.append(md.getColumnName(i));
					result.append(' ');
	
					String type = processType(md.getColumnTypeName(i), md
							.getPrecision(i));
					result.append(type);
	
					// if (md.getPrecision(i) < 65535)
					// {
					// result.append('(');
					// result.append(md.getPrecision(i));
					// if (md.getScale(i) > 0)
					// {
					// result.append(',');
					// result.append(md.getScale(i));
					// }
					// result.append(") ");
					// } else {
					// result.append(' ');
					// }
	
					// if (this.isNumeric(md.getColumnType(i))) {
					// if (!md.isSigned(i)) {
					// result.append("UNSIGNED ");
					// }
					// }
	
					if (md.isNullable(i) == ResultSetMetaData.columnNoNulls) {
						result.append("NOT NULL ");
					} else {
						// result.append("NULL ");
					}
					if (md.isAutoIncrement(i))
						result.append(" auto_increment");
				}
	
				DatabaseMetaData dbm = connection.getMetaData();
				ResultSet primary = dbm.getPrimaryKeys(null, null, table);
				boolean first = true;
				while (primary.next()) {
					if (first) {
						first = false;
						result.append(',');
						result.append("PRIMARY KEY(");
					} else
						result.append(",");
	
					result.append(primary.getString("COLUMN_NAME"));
				}
	
				if (!first)
					result.append(')');
	
				result.append(" ); ");
	
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				throw (new DatabaseException(e));
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					throw (new DatabaseException(e));
				}
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException e) {
					throw (new DatabaseException(e));
				}
			}
		}
		
		return result.toString();
	}

	/**
	 * Execute a INSERT, DELETE, UPDATE, or other statement that does not return
	 * a ResultSet.
	 * 
	 * @param sql
	 *            The query to execute.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public void execute(String sql) throws DatabaseException {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Syntax error? in " + sql);
			throw (new DatabaseException(e));
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get a list of all tables in the database.
	 * 
	 * @return A list of all tables in the database.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public Collection<String> listTables() throws DatabaseException {
		Collection<String> result = new ArrayList<String>();
		ResultSet rs = null;

		try {
			DatabaseMetaData dbm;
			dbm = connection.getMetaData();

			String types[] = { "TABLE" };
			rs = dbm.getTables(null, null, "", types);

			while (rs.next()) {
				String str = rs.getString("TABLE_NAME");
				result.add(str);
			}
			rs.close();
		} catch (SQLException e) {
			throw (new DatabaseException(e));
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}

		return result;
	}

	/**
	 * Determine if a table exists.
	 * 
	 * @param table
	 *            The name of the table.
	 * @return True if the table exists.
	 * @throws DatabaseException
	 *             A database error occured.
	 */
	public boolean tableExists(String table) throws DatabaseException {
		boolean result = false;
		ResultSet rs = null;

		try {
			DatabaseMetaData dbm;
			dbm = connection.getMetaData();
			
			String types[] = { "TABLE" };
			rs = dbm.getTables(null, null, table, types);
			result = rs.next();
			rs.close();
		} catch (SQLException e) {
			throw (new DatabaseException(e));
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	/**
	 * Get a list of all of the columns on a table.
	 * 
	 * @param table
	 *            The table to check.
	 * @return A list of all of the columns.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public Collection<String> listColumns(String table)
			throws DatabaseException {
		Collection<String> result = new ArrayList<String>();
		ResultSet rs = null;

		try {
			DatabaseMetaData dbm;
			dbm = connection.getMetaData();
			rs = dbm.getColumns(null, null, table, null);
			while (rs.next()) {
				result.add(rs.getString("COLUMN_NAME"));
			}
			rs.close();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	/**
	 * Create a prepared statement.
	 * 
	 * 
	 * @param sql
	 *            The SQL of the prepared statement.
	 * @return The PreparedStatement that was created.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public PreparedStatement prepareStatement(String sql)
			throws DatabaseException {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw (new DatabaseException(e));
		}
		return statement;
	}


	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
}
