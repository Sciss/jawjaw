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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Word;

/**
 * Very simple test to verify if something returns from DB. The content is not checked.
 * @author Hideki Shima
 *
 */
public class WordDAOTest {

	private String word1 = "自然言語処理";
	private String word2a = "natural_language_processing";
	private String word2b = "natural language processing";
	private String word2c = "Natural Language Processing";
	private int wordid = 201821;
		
	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.WordDAO#findWordsByLemma(java.lang.String)}.
	 */
	@Test
	public void testFindWordsByLemma() {
		List<Word> words1 = WordDAO.findWordsByLemma( word1 );
		Assert.assertTrue( words1.size() > 0 );
		List<Word> words2a = WordDAO.findWordsByLemma( word2a );
		Assert.assertTrue( words2a.size() > 0 );
		List<Word> words2b = WordDAO.findWordsByLemma( word2b );
		Assert.assertTrue( words2b.size() > 0 );
		List<Word> words2c = WordDAO.findWordsByLemma( word2c );
		Assert.assertTrue( words2c.size() > 0 );
	}

	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.WordDAO#findWordsByLemmaAndPos(java.lang.String, edu.cmu.lti.jawjaw.pobj.POS)}.
	 */
	@Test
	public void testFindWordsByLemmaAndPos() {
		List<Word> words = WordDAO.findWordsByLemmaAndPos( word1, POS.n );
		Assert.assertTrue( words.size() > 0 );
	}

	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.WordDAO#findWordByWordid(int)}.
	 */
	@Test
	public void testFindWordByWordid() {
		Word word = WordDAO.findWordByWordid( wordid );
		Assert.assertTrue( word != null );
	}

}
