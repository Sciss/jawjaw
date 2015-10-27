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

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * Data Access Object for synset table
 * @author Hideki Shima
 *
 */
public class SynsetDAO {
	
	private static ConcurrentMap<String, List<Synset>> cache;

	static {
		if (Configuration.getInstance().useCache()) {
			cache = new ConcurrentHashMap<String, List<Synset>>(
				Configuration.getInstance().getMaxCacheSize());
		}
	}
	
	/**
	 * Find synset record by synset id key
	 * @param synset id e.g. 06142412-n
	 * @return synset record
	 */
	public static Synset findSynsetBySynset( String synset ) {
		String key = synset;
		if ( Configuration.getInstance().useCache() ) {
			List<Synset> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj).get(0);
		}
		
		Synset s = null;
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNSET_BY_SYNSET );
			synchronized (ps) {
				ps.setString(1, synset);
				rs = ps.executeQuery();
				if ( rs.next() ) {
					s = rsToObject(rs);
				}
				rs.close();
//				ps.close();
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
//			synchronized ( cache ) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				List<Synset> synsets = new ArrayList<Synset>(1);
				synsets.add(s);
				if ( synsets != null ) cache.put( key, clone(synsets) );
//			}
		}
		return s;
	}
	
	/**
	 * Find synset records by name. This method is deprecated 
	 * since synset only has a name in English. Start from word
	 * record instead.
	 * 
	 * @param name label on a synset
	 * @return synset records
	 */
	@Deprecated
	public static List<Synset> findSynsetsByName( String name ) {
		String key = name;
		if ( Configuration.getInstance().useCache() ) {
			List<Synset> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Synset> synsets = new ArrayList<Synset>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNSETS_BY_NAME );
			synchronized (ps) {
				ps.setString(1, name);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					synsets.add( rsToObject(rs) );
				}
				rs.close();
//				ps.close();
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
//			synchronized ( cache ) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( synsets != null ) cache.put( key, clone(synsets) );
//			}
		}
		return synsets;
	}
	
	/**
	 * Find synset records by name and POS. This method is deprecated 
	 * since synset only has a name in English. Start from word
	 * record instead.
	 * 
	 * @param name
	 * @param pos
	 * @return synset records
	 */
	public static List<Synset> findSynsetsByNameAndPos( String name, POS pos ) {
		String key = name+" "+pos;
		if ( Configuration.getInstance().useCache() ) {
			List<Synset> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Synset> synsets = new ArrayList<Synset>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNSETS_BY_NAME_AND_POS );
			synchronized (ps) {
				ps.setString(1, name);
				ps.setString(2, pos.toString());
				rs = ps.executeQuery();
				while ( rs.next() ) {
					synsets.add( rsToObject(rs) );
				}
				rs.close();
//				ps.close();
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
//			synchronized ( cache ) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( synsets != null ) cache.put( key, clone(synsets) );
//			}
		}
		return synsets;
	}
	
	private static Synset rsToObject( ResultSet rs ) throws SQLException {
		Synset synset = new Synset(
			rs.getString(1),	
			POS.valueOf( rs.getString(2) ),
			rs.getString(3),
			rs.getString(4)	
		);
		return synset;
	}
	
	private static List<Synset> clone( List<Synset> synsets ) {
		List<Synset> cloned = new ArrayList<Synset>( synsets.size() );
		for ( Synset synset : synsets ) {
			cloned.add( synset.clone() );
		}
		return synsets;
	}
	
}
