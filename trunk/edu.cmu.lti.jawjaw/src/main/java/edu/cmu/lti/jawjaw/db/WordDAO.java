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
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Word;
import edu.cmu.lti.jawjaw.util.Configuration;
import edu.cmu.lti.jawjaw.util.TextUtil;

/**
 * Data Access Object for word table
 * @author hideki
 *
 */
public class WordDAO {

	private static ConcurrentMap<String, List<Word>> cache;

	static {
		if (Configuration.getInstance().useCache()) {
			cache = new ConcurrentHashMap<String, List<Word>>(
				Configuration.getInstance().getMaxCacheSize());
		}
	}
	
	/**
	 * Find words by lemma
	 * @param lemma cannonical form of the word either in Japanese or English
	 * @return word records
	 */
	public static List<Word> findWordsByLemma( String lemma ) {
		String key = lemma;
		if ( Configuration.getInstance().useCache() ) {
			List<Word> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		//Canonicalization
		lemma = TextUtil.canonicalize( lemma );
		
		List<Word> words = new ArrayList<Word>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_WORD_BY_LEMMA );
			synchronized (ps) {
				ps.setString(1, lemma);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					words.add( rsToObject(rs) );
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
				if ( words != null ) cache.put( key, clone(words) );
//			}
		}
		return words;
	}
	
	/**
	 * Find words by lemma and POS. 
	 * lemma can be either in English or Japanese
	 * 
	 * @param lemma cannonical form of the word either in Japanese or English
	 * @param pos POS of the lemma
	 * @return word records
	 */
	public static List<Word> findWordsByLemmaAndPos( String lemma, POS pos ) {
		String key = lemma+" "+pos;
		if ( Configuration.getInstance().useCache() ) {
			List<Word> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj);
		}
		
		List<Word> words = new ArrayList<Word>();
		if ( lemma == null ) return words;
		lemma = lemma.toLowerCase();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_WORD_BY_LEMMA_AND_POS );
			synchronized (ps) {
				ps.setString(1, lemma);
				ps.setString(2, pos.toString());
				rs = ps.executeQuery();
				while ( rs.next() ) {
					words.add( rsToObject(rs) );
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
				if ( words != null ) cache.put( key, clone(words) );
//			}
		}
		return words;
	}
	
	/**
	 * Find word by word id
	 * @param wordid
	 * @return word record
	 */
	public static Word findWordByWordid( int wordid ) {
		String key = wordid+"";
		if ( Configuration.getInstance().useCache() ) {
			List<Word> cachedObj = cache.get(key);
			if ( cachedObj != null ) return clone(cachedObj).get(0);
		}
		
		Word word = null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = SQL.getInstance().getPreparedStatement( SQLQuery.FIND_WORD_BY_WORDID );
			synchronized (ps) {
				ps.setInt(1, wordid);
				rs = ps.executeQuery();
				if ( rs.next() ) {
					word = rsToObject(rs);
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
			List<Word> words = new ArrayList<Word>(1);
			words.add(word);
//			synchronized ( cache ) {
				if (cache.size() >= Configuration.getInstance().getMaxCacheSize()) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if ( word != null ) cache.put( key, clone(words) );
//			}
		}
		
		return word;
	}
	
	private static Word rsToObject( ResultSet rs ) throws SQLException {
		Word word = new Word(
			rs.getInt(1),	
			Lang.valueOf( rs.getString(2) ),
			rs.getString(3),
			rs.getString(4),	
			POS.valueOf( rs.getString(5) )	
		);
		return word;
	}
	
	//FIXME:
	private static List<Word> clone( List<Word> words ) {
		List<Word> cloned = new ArrayList<Word>( words.size() );
		for ( Word word : words ) {
			cloned.add( word.clone() );
		}
		return words;
	}
}
