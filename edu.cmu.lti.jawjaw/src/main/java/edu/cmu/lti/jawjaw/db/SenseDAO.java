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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.Sense;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * Data Access Object for sense table 
 * @author Hideki Shima
 *
 */
public class SenseDAO {

	private static ConcurrentMap<String, List<Sense>> cache;

	static {
		if (Configuration.getInstance().useCache()) {
			cache = new ConcurrentHashMap<String, List<Sense>>(
				Configuration.getInstance().getMaxCacheSize());
		}
	}
	
	/**
	 * Find sense records by synset (one-to-many relationship)
	 * @param synset synset id e.g. 06142412-n
	 * @return sense records
	 */
	public static List<Sense> findSensesBySynset( String synset ) {
		String key = synset;
		if ( Configuration.getInstance().useCache() ) {
			List<Sense> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Sense> senses = new ArrayList<Sense>(); 
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SENSES_BY_SYNSET );
			synchronized (ps) { 
				ps.setString(1, synset);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					senses.add( rsToObject(rs) );
				}
				rs.close();
	//			ps.close();
				rs = null;
//				ps = null;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if ( rs != null ) rs.close();
//				if ( ps != null ) ps.close();
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		if (Configuration.getInstance().useCache()) {
//			synchronized (cache) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( senses != null ) cache.put( key, clone(senses) );
//			}
		}
		return senses;
	}
	
	/**
	 * Find sense records by word id (one-to-many relationship)
	 * @param wordid word id
	 * @return sense records
	 */
	public static List<Sense> findSensesByWordid( int wordid ) {
		String key = wordid+"";
		if ( Configuration.getInstance().useCache() ) {
			List<Sense> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Sense> senses = new ArrayList<Sense>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			try {
				ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SENSES_BY_WORDID );
				synchronized (ps) {
					ps.setInt(1, wordid);
					rs = ps.executeQuery();
					while ( rs.next() ) {
						senses.add( rsToObject(rs) );
					}
					rs.close();
	//				ps.close();
					rs = null;
//					ps = null;
				}
			} finally {
				if ( rs != null ) rs.close(); 
//				if ( ps != null ) ps.close(); 
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		if (Configuration.getInstance().useCache()) {
//			synchronized (cache) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( senses != null ) cache.put( key, clone(senses) );
//			}
		}
		return senses;
	}
	
	/**
	 * Find sense records by synset and language (one-to-many relationship)
	 * @param synset synset id e.g. 06142412-n
	 * @param lang either "eng" or "jpn"
	 * @return sense records
	 */
	public static List<Sense> findSensesBySynsetAndLang( String synset, Lang lang ) {
		String key = synset+" "+lang;
		if ( Configuration.getInstance().useCache() ) {
				List<Sense> cachedObj = cache.get(key);
				if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Sense> senses = new ArrayList<Sense>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SENSES_BY_SYNSET_AND_LANG );
			synchronized (ps) {
				ps.setString(1, synset);
				ps.setString(2, lang.toString());
				rs = ps.executeQuery();
				while ( rs.next() ) {
					senses.add( rsToObject(rs) );
				}
				rs.close();
	//			ps.close();
				rs = null;
//				ps = null;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		} finally {
			try {
				if ( rs != null ) rs.close();
//				if ( ps != null ) ps.close();
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		if (Configuration.getInstance().useCache()) {
//			synchronized (cache) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( senses != null ) cache.put( key, clone(senses) );
//			}
		}
		return senses;
	}
	
	private static Sense rsToObject( ResultSet rs ) throws SQLException {
		Sense sense = new Sense(
			rs.getString(1),	
			rs.getInt(2),	
			Lang.valueOf( rs.getString(3) ),
			rs.getInt(4),
			rs.getInt(5),
			rs.getInt(6),
			rs.getString(7)	
		);
		return sense;
	}
	
	private static List<Sense> clone( List<Sense> senses ) {
		List<Sense> cloned = new ArrayList<Sense>( senses.size() );
		for ( Sense sense : senses ) {
			cloned.add( sense.clone() );
		}
		return senses;
	}
}
