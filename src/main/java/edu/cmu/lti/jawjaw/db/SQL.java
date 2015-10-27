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

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.cmu.lti.jawjaw.db.datamover.DataMoverUtility;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * This class is responsible for communicating with the database
 * @author Hideki Shima
 *
 */
final public class SQL {

	private static Connection connection;
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final SQL instance = new SQL(); // this is last. order matters!!
	private static final boolean BENCHMARK = false;
	
	private ConcurrentMap<SQLQuery,PreparedStatement> preparedStatements = null;
	
	/**
	 * Private constructor 
	 */
	private SQL() {
		try {
			createSQLConnection();
			prepareStatements();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Singleton pattern
	 * @return singleton object
	 */
	public static SQL getInstance(){
		return SQL.instance;
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
//		String pathToWordNet = URLDecoder.decode(dbUrl.getPath(), "UTF-8");
//		pathToWordNet = extractDBIfJar( pathToWordNet );
//		
//		String sqlUrl = "jdbc:sqlite:"+pathToWordNet;
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
	
	private synchronized void prepareStatements() throws SQLException {
		preparedStatements = new ConcurrentHashMap<SQLQuery, PreparedStatement>();
		for ( SQLQuery query : SQLQuery.values() ) {
			String queryText = query.getQueryText();
			PreparedStatement ps = connection.prepareStatement( queryText );
			preparedStatements.put( query, ps );
		}
	}
	
	/**
	 * Unused!
	 * Since v1.0.2, "jdbc:sqlite::resource:" doesn't require extracting 
	 * a file from jar as long as it's in classpath. 
	 * 
	 * @param pathToWordNet
	 * @return extracted path
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
  private String extractDBIfJar( String pathToWordNet ) throws IOException {
	  final String tempPrefix = "wnja-temp-";
		// If not in jar, short circuit.
		if ( pathToWordNet.indexOf("jar!")==-1 ) {
			return pathToWordNet;
		}
		
		System.out.print( "Extracting wordnet database from jar file ... " );
		long t0 = System.currentTimeMillis();
		String wnName = Configuration.getInstance().getWordnet();
		
		String tempFolderPath = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
		File tempFolder = new File(tempFolderPath);
		
		// terminate old temp files not deleted 
		File[] oldTempFiles = tempFolder.listFiles(new FileFilter() {
		  public boolean accept(File file) {
	        return ( file.getName().startsWith(tempPrefix) );
	      }
	    });
		for ( File f : oldTempFiles ) {
			boolean deleted = f.delete();
			if (!deleted) {
				System.out.print( "Failed to delete old wn-ja files at "+f.getAbsolutePath() );
			}
		}
		
		File uncompressedDb = File.createTempFile(tempPrefix, ".db");
		uncompressedDb.deleteOnExit();
				
		try {
			InputStream reader = SQL.class.getResourceAsStream("/"+wnName);
			FileOutputStream writer = new FileOutputStream(uncompressedDb);
	        byte[] buffer = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = reader.read(buffer)) != -1)
	        {
	            writer.write(buffer, 0, bytesRead);
	        }
	        writer.close();
	        reader.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		long t1 = System.currentTimeMillis();
		System.out.println( "done in "+(double)(t1-t0)/1000D + " sec." );
		return uncompressedDb.getAbsolutePath();
	}
	
	public PreparedStatement getPreparedStatement( SQLQuery query ) {
		return preparedStatements.get( query );
	}
		
	@Override
	protected void finalize() throws Throwable {
		try {
			for ( PreparedStatement ps : preparedStatements.values() ) {
				if ( ps != null ) ps.close();
			}
		} finally {
			if ( connection != null ) connection.close();
		}
	}
		
	public Connection getConnection() {
		return connection;
	}
}
