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

import java.util.HashMap;
import java.util.Map;

public enum SQLQuery {
	
	FIND_WORD_BY_LEMMA, 
	FIND_WORD_BY_LEMMA_AND_POS,
	FIND_WORD_BY_WORDID,
	
	FIND_SENSES_BY_SYNSET,
	FIND_SENSES_BY_WORDID,
	FIND_SENSES_BY_SYNSET_AND_LANG,
	
	FIND_SYNSETDEF_BY_SYNSET_AND_LANG,
	
	FIND_SYNLINK_BY_SYNSET,
	FIND_SYNLINK_BY_SYNSET_AND_LINK,
	
	FIND_SYNSET_BY_SYNSET,
	FIND_SYNSETS_BY_NAME,
	FIND_SYNSETS_BY_NAME_AND_POS;
	
	private static final Map<SQLQuery,String> queryTexts = new HashMap<SQLQuery, String>( SQLQuery.values().length );
	static {
		queryTexts.put( FIND_WORD_BY_LEMMA,         "SELECT * FROM word WHERE lemma=?" );
		queryTexts.put( FIND_WORD_BY_LEMMA_AND_POS, "SELECT * FROM word WHERE lemma=? AND pos=?" );
		queryTexts.put( FIND_WORD_BY_WORDID,        "SELECT * FROM word WHERE wordid=?" );
		
		queryTexts.put( FIND_SENSES_BY_SYNSET,          "SELECT * FROM sense WHERE synset=?" );
		queryTexts.put( FIND_SENSES_BY_WORDID,          "SELECT * FROM sense WHERE wordid=?" );
		queryTexts.put( FIND_SENSES_BY_SYNSET_AND_LANG, "SELECT * FROM sense WHERE synset=? AND lang=?" );
		
		queryTexts.put( FIND_SYNSETDEF_BY_SYNSET_AND_LANG, "SELECT * FROM synset_def WHERE synset=? AND lang=?" );
		
		queryTexts.put( FIND_SYNLINK_BY_SYNSET,          "SELECT * FROM synlink WHERE synset1=?" );
		queryTexts.put( FIND_SYNLINK_BY_SYNSET_AND_LINK, "SELECT * FROM synlink WHERE synset1=? AND link=?" );
		
		queryTexts.put( FIND_SYNSET_BY_SYNSET,        "SELECT * FROM synset WHERE synset=?" );
		queryTexts.put( FIND_SYNSETS_BY_NAME,         "SELECT * FROM synset WHERE name=?" );
		queryTexts.put( FIND_SYNSETS_BY_NAME_AND_POS, "SELECT * FROM synset WHERE name=? AND pos=?" );
	}
	
	public String getQueryText() {
		return queryTexts.get( this );
	}
}
