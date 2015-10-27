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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;
import edu.cmu.lti.jawjaw.util.Configuration;

/**
 * Data Access Object for synset_def table.
 * Note that definition is available in English with wnjpn-0.9
 * @author Hideki Shima
 *
 */
public class SynsetDefDAO {

	private static ConcurrentMap<String, SynsetDef> cache;

	static {
		if (Configuration.getInstance().useCache()) {
			cache = new ConcurrentHashMap<String, SynsetDef>(
				Configuration.getInstance().getMaxCacheSize());
		}
	}
	
	/**
	 * Find synset definition record by synset and lang
	 * @param synset e.g. 06142412-n
	 * @param lang
	 * @return synset definition
	 */
	public static SynsetDef findSynsetDefBySynsetAndLang( String synset, Lang lang ) {
		String key = synset+" "+lang.toString();
		if ( Configuration.getInstance().useCache() ) {
			SynsetDef cachedObj = cache.get(key);
			if ( cachedObj != null ) return cachedObj.clone();
		}
		
		SynsetDef synsetDef = null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_SYNSETDEF_BY_SYNSET_AND_LANG );
			synchronized (ps) {
				ps.setString(1, synset);
				ps.setString(2, lang.toString());
				rs = ps.executeQuery();
				if ( rs.next() ) {
					synsetDef = rsToObject(rs);
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
				if ( synsetDef != null ) cache.put( key, synsetDef.clone() );
//			}
		}
		return synsetDef;
	}
	
	private static SynsetDef rsToObject( ResultSet rs ) throws SQLException {
		SynsetDef synsetDef = new SynsetDef(
			rs.getString(1),	
			Lang.valueOf( rs.getString(2) ),
			rs.getString(3),
			rs.getInt(4)	
		);
		return synsetDef;
	}
	
}
