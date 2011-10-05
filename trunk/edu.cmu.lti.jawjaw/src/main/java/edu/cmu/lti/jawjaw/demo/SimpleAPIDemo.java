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

import java.util.Set;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.pobj.POS;

public class SimpleAPIDemo {
	private static void run( String word, POS pos ) {
		// 日本語 WordNet から情報を抽出
		Set<String> synonyms = JAWJAW.findSynonyms(word, pos);
		Set<String> hypernyms = JAWJAW.findHypernyms(word, pos);
		Set<String> hyponyms = JAWJAW.findHyponyms(word, pos);
		Set<String> meronyms = JAWJAW.findMeronyms(word, pos);
		Set<String> holonyms = JAWJAW.findHolonyms(word, pos);
		Set<String> translations = JAWJAW.findTranslations(word, pos);
		Set<String> definitions = JAWJAW.findDefinitions(word, pos);

		System.out.println( "synonyms of "+word+" : \t"+ synonyms );
		System.out.println( "hypernyms of "+word+" : \t"+ hypernyms );
		System.out.println( "hyponyms of "+word+" : \t"+ hyponyms );
		System.out.println( "meronyms of "+word+" : \t"+ meronyms );
		System.out.println( "holonyms of "+word+" : \t"+ holonyms );
		System.out.println( "translations of "+word+" : \t"+ translations );
		System.out.println( "definitions of "+word+" : \t"+ definitions );		
	}
	public static void main(String[] args) {
		SimpleAPIDemo.run( "author", POS.n );
	}
}