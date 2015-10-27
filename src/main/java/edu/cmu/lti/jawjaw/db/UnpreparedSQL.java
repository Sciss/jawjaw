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
package edu.cmu.lti.jawjaw.db;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import edu.cmu.lti.jawjaw.db.datamover.DataMoverUtility;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * This class is responsible for communicating with the database.
 * Need to benchmark whether this version without prepared-statement
 * is faster. Unless the same query is not recycled over and over,
 * statement preparation is costly...?   
 *  
 * @author Hideki Shima
 *
 */
final public class UnpreparedSQL {

	private static Connection connection;
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final UnpreparedSQL instance = new UnpreparedSQL(); // this is last. order matters!!
  private static final boolean BENCHMARK = false;
  
	/**
	 * Private constructor 
	 */
	private UnpreparedSQL() {
		try {
			createSQLConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Singleton pattern
	 * @return singleton object
	 */
	public static UnpreparedSQL getInstance(){
		return UnpreparedSQL.instance;
	}
	
	/**
	 * Creates a jdbc connection to a sqlite3 db
	 * @throws Exception
	 */
	private synchronized void createSQLConnection() throws ClassNotFoundException, 
		UnsupportedEncodingException, IOException, SQLException {	
		// Classload the driver
		Class.forName( DRIVER );
		
	// Doesn't work with "/data/wnjpn-0.9.db", "wnjpn-0.9.db"
    String classpath = "/"+Configuration.getInstance().getWordnet();
    URL dbUrl = SQL.class.getResource( classpath );
    if (dbUrl==null) {
      System.err.println("ERROR: Make sure the NICT wordnet db is stored in classpath at: "+classpath);
    }
//    String pathToWordNet = URLDecoder.decode(dbUrl.getPath(), "UTF-8");
//    pathToWordNet = extractDBIfJar( pathToWordNet );
//    
//    String sqlUrl = "jdbc:sqlite:"+pathToWordNet;
    String sqlUrl = "jdbc:sqlite::resource:"+Configuration.getInstance().getWordnet();
    
		// Memory DB mode is super fast after the initialization. 
		if ( Configuration.getInstance().useMemoryDB() ) {
			connection = DataMoverUtility.getMemoryDBConnection( DRIVER, sqlUrl );
		} else {
			connection = DriverManager.getConnection( sqlUrl );
		}
		//synchronized ( connection ) {
			createIndexIfNotExists( connection );
			setPragmaCacheSize( connection );
		//}
	}
	
	private void createIndexIfNotExists( Connection connection ) {
		long t0 = System.currentTimeMillis();
		if (BENCHMARK) System.out.print( "Building index on DB ... " );
		Statement s = null;
		try {
			s = connection.createStatement();
			connection.setAutoCommit(false);
			s.addBatch( "CREATE INDEX IF NOT EXISTS word_wordid_idx ON word (wordid);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS word_lemma_idx ON word (lemma,pos);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS sense_synset_idx ON sense (synset);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS sense_wordid_idx ON sense (wordid);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS synset_id_idx ON synset (synset);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS synset_name_idx ON synset (name);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS synset_def_id_idx ON synset_def (synset);" );
			s.addBatch( "CREATE INDEX IF NOT EXISTS synlink_idx ON synlink (synset1,link);" );
			s.executeBatch();
			connection.setAutoCommit(true);
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if ( s != null ) s.close(); 
			} catch ( SQLException e2 ) { e2.printStackTrace(); }
		}
		long t1 = System.currentTimeMillis();
		if (BENCHMARK) System.out.println( "done in "+((double)(t1-t0)/1000D)+" sec." );
	}
	
	private void setPragmaCacheSize( Connection connection ) {
		// On-memory db doesn't need this parameter.
		if ( Configuration.getInstance().useMemoryDB() ) return;
		Statement s = null;
		try {
			s = connection.createStatement();
			s.execute( "PRAGMA cache_size = "+Configuration.getInstance().getDbCacheSize()+";" );
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if ( s != null ) s.close(); 
			} catch ( SQLException e2 ) { e2.printStackTrace(); }
		}
	}
	
	public PreparedStatement getPreparedStatement( SQLQuery query ) throws SQLException {
		return connection.prepareStatement( query.getQueryText() );
	}
		
}
