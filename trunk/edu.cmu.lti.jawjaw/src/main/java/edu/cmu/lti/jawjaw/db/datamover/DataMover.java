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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generic data mover class. This class is designed to move data from one
 * database to another. To do this, first the tables are created in the target
 * database, then all data from the source database is copied.
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 * 
 */
public class DataMover {
	/**
	 * The source database.
	 */
	private Database source;

	/**
	 * The target database.
	 */
	private Database target;

	/**
	 * The list of tables, from the source database.
	 */
	private List<String> tables = new ArrayList<String>();

	public Database getSource() {
		return source;
	}

	public void setSource(Database source) {
		this.source = source;
	}

	public Database getTarget() {
		return target;
	}

	public void setTarget(Database target) {
		this.target = target;
	}

	/**
	 * Create the specified table. To do this the source database will be
	 * scanned for the table's structure. Then the table will be created in the
	 * target database.
	 * 
	 * @param table
	 *            The name of the table to create.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	public synchronized void createTable(String table) throws DatabaseException {
		String sql;

		// if the table already exists, then drop it
		if (target.tableExists(table)) {
			sql = source.generateDrop(table);
			target.execute(sql);
		}

		// now create the table
		sql = source.generateCreate(table);
		target.execute(sql);
	}

	/**
	 * Create all of the tables in the database. This is done by looping over
	 * the list of tables and calling createTable for each.
	 * 
	 * @throws DatabaseException
	 *             If an error occurs.
	 */
	private synchronized void createTables() throws DatabaseException {
		Collection<String> list = source.listTables();
		for (String table : list) {
			try {
				createTable(table);
				tables.add(table);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Copy the data from one table to another. To do this both a SELECT and
	 * INSERT statement must be created.
	 * 
	 * @param table
	 *            The table to copy.
	 * @throws DatabaseException
	 *             If a database error occurs.
	 */
	private synchronized void copyTable(String table) throws DatabaseException {
		StringBuffer selectSQL = new StringBuffer();
		StringBuffer insertSQL = new StringBuffer();
		StringBuffer values = new StringBuffer();

		Collection<String> columns = source.listColumns(table);

		selectSQL.append("SELECT ");
		insertSQL.append("INSERT INTO ");
		insertSQL.append(table);
		insertSQL.append("(");

		boolean first = true;
		for (String column : columns) {
			if (!first) {
				selectSQL.append(",");
				insertSQL.append(",");
				values.append(",");
			} else
				first = false;

			selectSQL.append(column);
			insertSQL.append(column);
			values.append("?");
		}
		selectSQL.append(" FROM ");
		selectSQL.append(table);

		insertSQL.append(") VALUES (");
		insertSQL.append(values);
		insertSQL.append(")");

		// now copy
		PreparedStatement statementSrc = null;
		PreparedStatement statementTrg = null;
		ResultSet rs = null;

		try {
			statementTrg = target.prepareStatement(insertSQL.toString());
			statementSrc = source.prepareStatement(selectSQL.toString());
			rs = statementSrc.executeQuery();

			int rows = 0;

			while (rs.next()) {
				rows++;
				for (int i = 1; i <= columns.size(); i++) {
					statementTrg.setString(i, rs.getString(i));
				}
				statementTrg.execute();
			}
			rs.close();
			statementSrc.close();
			statementTrg.close();
		} catch (SQLException e) {
			throw (new DatabaseException(e));
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (SQLException e) {
				throw (new DatabaseException(e));
			}
			try {
				if (statementSrc != null) statementSrc.close();
			} catch (SQLException e) {
				throw (new DatabaseException(e));
			}
			try {
				if (statementTrg != null) statementTrg.close();
			} catch (SQLException e) {
				throw (new DatabaseException(e));
			}
		}
	}

	private void copyTableData() throws DatabaseException {
		for (String table : tables) {
			copyTable(table);
		}
	}

	public void exportDatabse() throws DatabaseException {
		createTables();
		copyTableData();
	}

}
