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

import java.sql.Connection;


/**
 * A utility to copy data from two databases, as specified by a configuration
 * file. The configuration file is of the format:
 * 
 * sourceDriver=com.mysql.jdbc.Driver
 * sourceURL=jdbc:mysql://127.0.0.1/test?user=root
 * targetDriver=com.mysql.jdbc.Driver
 * targetURL=jdbc:mysql://127.0.0.1/test2?user=root
 * 
 * @author Jeff Heaton (http://www.heatonresearch.com)
 * 
 */
public class DataMoverUtility {

	public static void copyDB( String sourceDriver, String sourceURL, 
			String targetDriver, String targetURL ) {
		try {
			DataMover mover = new DataMover();

			Database source = new SQLite();
			source.connect(sourceDriver, sourceURL);

			Database target = new SQLite();
			target.connect(targetDriver, targetURL);

			mover.setSource(source);
			mover.setTarget(target);
			mover.exportDatabse();

			source.close();
			target.close();

		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getMemoryDBConnection( String sourceDriver, String sourceURL ) {
		Connection connection = null;
		String targetURL = "jdbc:sqlite::memory:";
		try {
			DataMover mover = new DataMover();

			Database source = new SQLite();
			source.connect(sourceDriver, sourceURL);

			Database target = new SQLite();
			target.connect(sourceDriver, targetURL);

			mover.setSource(source);
			mover.setTarget(target);
			System.out.print( "Loading WordNet DB into memory ... " );
			long t0 = System.currentTimeMillis();
			mover.exportDatabse();
			long t1 = System.currentTimeMillis();
			System.out.println( "done in "+((double)(t1-t0)/1000D)+" sec." );
			source.close();
			
			// Below was somehow missing before Jun
			connection = target.getConnection();
		} catch ( DatabaseException e ) {
			e.printStackTrace();
		} 
		return connection;
	}
}
