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
package edu.cmu.lti.jawjaw.demo;

import java.util.List;

import edu.cmu.lti.jawjaw.db.SenseDAO;
import edu.cmu.lti.jawjaw.db.SynlinkDAO;
import edu.cmu.lti.jawjaw.db.SynsetDAO;
import edu.cmu.lti.jawjaw.db.SynsetDefDAO;
import edu.cmu.lti.jawjaw.db.WordDAO;
import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Sense;
import edu.cmu.lti.jawjaw.pobj.Synlink;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;
import edu.cmu.lti.jawjaw.pobj.Word;

public class AdvancedAPIDemo {
	private static void run( String word, POS pos ) {
		// 日本語 WordNet から情報を抽出
		List<Word> words = WordDAO.findWordsByLemmaAndPos(word, pos);
		List<Sense> senses = SenseDAO.findSensesByWordid( words.get(0).getWordid() );
		String synsetId = senses.get(0).getSynset();
		Synset synset = SynsetDAO.findSynsetBySynset( synsetId );
		SynsetDef synsetDef = SynsetDefDAO.findSynsetDefBySynsetAndLang(synsetId, Lang.eng);
		List<Synlink> synlinks = SynlinkDAO.findSynlinksBySynset( synsetId );
		// 結果表示（多義語はごっちゃになっています）
		System.out.println( words.get(0) );
		System.out.println( senses.get(0) );
		System.out.println( synset );
		System.out.println( synsetDef );
		System.out.println( synlinks.get(0) );
	}
	public static void main(String[] args) {
		// "自然言語処理"(名詞)という単語から得られる関係の一部をデモします
		AdvancedAPIDemo.run( "自然言語処理", POS.n );
	}
}