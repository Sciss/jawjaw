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

/**
 * Database class for the MySQL database.
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 * 
 */
public class SQLite extends Database {

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
	public String processType(String type, int size) {
		String usigned = "UNSIGNED";
		int i = type.indexOf(usigned);
		if (i != -1)
			type = type.substring(0, i) + type.substring(i + usigned.length());

		if (type.equalsIgnoreCase("varchar") && (size == 65535))
			type = "TEXT";

		return type.trim();
	}
}
