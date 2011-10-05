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
package edu.cmu.lti.jawjaw.util;

import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static final Configuration instance = new Configuration();
	private Properties p;
	private String wordnet;
	private boolean cache;
	private int maxCacheSize;
	private final static String CONF = "/jawjaw.conf";
	private boolean memoryDB;
	private int dbCacheSize;
	
	/**
	 * Private constructor 
	 */
	private Configuration(){
		InputStream stream = null;
		try {
			stream = Configuration.class.getResourceAsStream( CONF );
			p = new Properties();
			p.load( stream );
			wordnet = readString("wordnet", "wnjpn-0.9.db");
			cache = readInt("cache", 1)==1;
			maxCacheSize = readInt("maxCacheSize", 1000);
			memoryDB = readInt("memoryDB", 1)==1;
			dbCacheSize = readInt("dbCacheSize", 2000);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int readInt( String key, int defaultValue ) {
		try {
			return Integer.parseInt(readString(key, defaultValue+""));
		} catch ( Exception e ) {
			return defaultValue;
		}
	}
	
	private String readString( String key, String defaultValue ) {
		String value = p.getProperty(key);
		if ( value == null ) {
			System.err.println( "Configuration \""+key+"\" not found in "+CONF );
			return defaultValue;
		}
		value = value.replaceAll("#.+", "");
		value = value.trim();
		return value;
	}
	
	/**
	 * Singleton pattern
	 * @return singleton object
	 */
	public static Configuration getInstance(){
		return Configuration.instance;
	}

	/**
	 * @return the dbCacheSize
	 */
	public int getDbCacheSize() {
		return dbCacheSize;
	}
	
	/**
	 * @param dbCacheSize the dbCacheSize to set
	 */
	public void setDbCacheSize(int dbCacheSize) {
		this.dbCacheSize = dbCacheSize;
	}

	/**
	 * @return the wordnet
	 */
	public String getWordnet() {
		return wordnet;
	}

	public boolean useCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(boolean cache) {
		this.cache = cache;
	}
	
	/**
	 * @return the maxCacheSize
	 */
	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	/**
	 * @param maxCacheSize the maxCacheSize to set
	 */
	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
	
	/**
	 * @return the memoryDB
	 */
	public boolean useMemoryDB() {
		return memoryDB;
	}

	/**
	 * @param memoryDB the memoryDB to set
	 */
	public void setMemoryDB(boolean memoryDB) {
		this.memoryDB = memoryDB;
	}	
}
