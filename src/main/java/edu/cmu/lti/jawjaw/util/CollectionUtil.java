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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.cmu.lti.jawjaw.db.SenseDAO;
import edu.cmu.lti.jawjaw.db.SynsetDAO;
import edu.cmu.lti.jawjaw.db.WordDAO;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Synset;

public class CollectionUtil {

	private static ConcurrentMap<String,String> cache;
	public static int capacity;

	static {
		capacity = Configuration.getInstance().getMaxCacheSize();
		cache = new ConcurrentHashMap<String,String>( capacity );
	}
	
	@SuppressWarnings("deprecation")
	public static String synsetToOffset( String synset ) {
		if ( synset.equals("0") ) return "*Root*";
		
		String key = synset;
		if ( Configuration.getInstance().useCache() ) {
			String cachedObj = cache.get(key);
			if ( cachedObj != null ) return cachedObj;
		}
		
		Synset synsetObj = SynsetDAO.findSynsetBySynset( synset );
		String word = synsetObj.getName();
		int wordid = SenseDAO.findSensesBySynset( synset ).get(0).getWordid();
		POS pos = WordDAO.findWordByWordid( wordid ).getPos();
		List<Synset> synsets = SynsetDAO.findSynsetsByName( word );
		int index = 0;
		for ( int i=0; i<synsets.size(); i++ ) {
			if ( synsets.get(i).getSynset().equals( synset ) ) {
				index = i+1;
			}
		}
		
		String offset = word+"#"+pos.toString()+"#"+index;
		if ( Configuration.getInstance().useCache() ) {
//			synchronized ( cache ) {
				if ( cache.size() >= Configuration.getInstance().getMaxCacheSize() ) {
					cache.remove( cache.keySet().iterator().next() );
				}
				if (offset!=null) cache.put(key, offset);
//			}
		}
		return offset;
	}

	
}
