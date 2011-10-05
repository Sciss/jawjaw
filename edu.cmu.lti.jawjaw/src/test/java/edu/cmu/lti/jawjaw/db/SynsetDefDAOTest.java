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

import junit.framework.Assert;

import org.junit.Test;

import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;

/**
 * Very simple test to verify if something returns from DB. The content is not checked.
 * @author Hideki Shima
 *
 */
public class SynsetDefDAOTest {
	
	private String synset = "06142412-n";
	
	/**
	 * Test method for {@link edu.cmu.lti.jawjaw.db.SynsetDefDAO#findSynsetDefBySynsetAndLang(java.lang.String, edu.cmu.lti.jawjaw.pobj.Lang)}.
	 */
	@Test
	public void testFindSynsetDefBySynsetAndLang() {
		SynsetDef synsetDef = SynsetDefDAO.findSynsetDefBySynsetAndLang(synset, Lang.eng);
		Assert.assertTrue( synsetDef != null );
	}

}
