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

import edu.cmu.lti.jawjaw.pobj.Link;
import edu.cmu.lti.jawjaw.pobj.Synlink;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * Data Access Object for synlink table
 * @author Hideki Shima
 *
 */
public class SynlinkDAO {

	private static ConcurrentMap<String, List<Synlink>> cache;

	static {
		if (Configuration.getInstance().useCache()) {
			cache = new ConcurrentHashMap<String, List<Synlink>>(
				Configuration.getInstance().getMaxCacheSize());
		}
	}
	
	/**
	 * Find synlink records by synset (one-to-many relationship)
	 * @param synset e.g. 06142412-n
	 * @return synlink records
	 */
	public static List<Synlink> findSynlinksBySynset( String synset ) {
		String key = synset;
		if ( Configuration.getInstance().useCache() ) {
			List<Synlink> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Synlink> synlinks = new ArrayList<Synlink>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNLINK_BY_SYNSET );
			synchronized (ps) {
				ps.setString(1, synset);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					synlinks.add( rsToObject(rs) );
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
				if ( synlinks != null ) cache.put( key, clone(synlinks) );
//			}
		}
		
		return synlinks;
	}
	
	/**
	 * Find synlink records by synset and link (one-to-many relationship)
	 * @param synset first argument of a relationship e.g. 06142412-n
	 * @param link lexical relationship
	 * @return synlink records
	 */
	public static List<Synlink> findSynlinksBySynsetAndLink( String synset, Link link ) {
		String key = synset+" "+link.toString();
		if ( Configuration.getInstance().useCache() ) {
				List<Synlink> cachedObj = cache.get(key);
				if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Synlink> synlinks = new ArrayList<Synlink>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNLINK_BY_SYNSET_AND_LINK );
			synchronized (ps) {
				ps.setString(1, synset);
				ps.setString(2, link.toString());
				rs = ps.executeQuery();
				while ( rs.next() ) {
					synlinks.add( rsToObject(rs) );
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
				if ( synlinks != null ) cache.put( key, clone(synlinks) );
//			}
		}
		return synlinks;
	}
	
	private static Synlink rsToObject( ResultSet rs ) throws SQLException {
		Synlink synlink = new Synlink(
			rs.getString(1),	
			rs.getString(2),
			Link.valueOf( rs.getString(3) ),
			rs.getString(4)	
		);
		return synlink;
	}
	
	private static List<Synlink> clone( List<Synlink> synlinks ) {
		List<Synlink> cloned = new ArrayList<Synlink>( synlinks.size() );
		for ( Synlink synlink : synlinks ) {
			cloned.add( synlink.clone() );
		}
		return synlinks;
	}
}
