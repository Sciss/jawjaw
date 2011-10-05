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
import edu.cmu.lti.jawjaw.pobj.Synset;

/**
 * Very simple test to verify if something returns from DB. The content is not checked.
 * @author Hideki Shima
 *
 */
public class SynsetDAOTest {
	
	private String synset = "06142412-n";
	private String name = "import";
	
	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.SynsetDAO#findSynsetBySynset(java.lang.String)}.
	 */
	@Test
	public void testFindSynsetBySynset() {
		Synset synsetObj = SynsetDAO.findSynsetBySynset( synset );
		Assert.assertTrue( synsetObj != null );
	}

	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.SynsetDAO#findSynsetsByName(java.lang.String)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testFindSynsetsByName() {
		List<Synset> synsets = SynsetDAO.findSynsetsByName( name );
		Assert.assertTrue( synsets.size() > 0 );
	}

	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.SynsetDAO#findSynsetsByNameAndPos(java.lang.String, edu.cmu.lti.jawjaw.pobj.POS)}.
	 */
	@Test
	public void testFindSynsetsByNameAndPos() {
		List<Synset> synsets = SynsetDAO.findSynsetsByNameAndPos( name, POS.v );
		Assert.assertTrue( synsets.size() > 0 );
	}

}
