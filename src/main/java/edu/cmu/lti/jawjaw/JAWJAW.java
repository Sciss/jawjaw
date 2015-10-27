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
package edu.cmu.lti.jawjaw;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.lti.jawjaw.db.SynsetDefDAO;
import edu.cmu.lti.jawjaw.pobj.Lang;
import edu.cmu.lti.jawjaw.pobj.Link;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.pobj.SynsetDef;
import edu.cmu.lti.jawjaw.util.WordNetUtil;

/**
 * Java Wrapper for Japanese WordNet.
 * 
 * This is a facade class that provides simple APIs for end users.
 * For doing more complicated stuff, use DAO classes under the package edu.cmu.lti.jawjaw.dao
 * 
 * @author Hideki Shima
 *
 */
public class JAWJAW {
	
	/**
	 * Finds hypernyms of a word. According to <a href="http://en.wikipedia.org/wiki/WordNet">wikipedia</a>, 
	 * <ul>
	 * <li>(Noun) hypernyms: Y is a hypernym of X if every X is a (kind of) Y (canine is a hypernym of dog)</li>
	 * <li>(Verb) hypernym: the verb Y is a hypernym of the verb X if the activity X is a (kind of) Y (travel is an hypernym of movement)</li>
	 * </ul>
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return hypernyms
	 */
	public static Set<String> findHypernyms( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.hype);
	}
	/**
	 * Finds hyponyms of a word. According to <a href="http://en.wikipedia.org/wiki/WordNet">wikipedia</a>, 
	 * <ul>
	 * <li>(Noun) hyponyms: Y is a hyponym of X if every Y is a (kind of) X (dog is a hyponym of canine)
	 * </ul>
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return hyponyms
	 */
	public static Set<String> findHyponyms( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.hypo);
	}
	/**
	 * Finds meronyms of a word. According to <a href="http://en.wikipedia.org/wiki/WordNet">wikipedia</a>, 
	 * <ul>
	 * <li>(Noun) meronym: Y is a meronym of X if Y is a part of X (window is a meronym of building)
	 * </ul>
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return meronyms
	 */
	public static Set<String> findMeronyms( String word, POS pos ) {
		Set<String> results = new LinkedHashSet<String>();
		results.addAll( WordNetUtil.findLinks(word, pos, Link.mmem) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.msub) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.mprt) );
		return results;
	}
	/**
	 * Finds holonyms of a word. According to <a href="http://en.wikipedia.org/wiki/WordNet">wikipedia</a>, 
	 * <ul>
	 * <li>(Noun) holonym: Y is a holonym of X if X is a part of Y (building is a holonym of window)
	 * </ul>
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return holonyms
	 */
	public static Set<String> findHolonyms( String word, POS pos ) {
		Set<String> results = new LinkedHashSet<String>();
		results.addAll( WordNetUtil.findLinks(word, pos, Link.hmem) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.hsub) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.hprt) );
		return results;
	}
	/**
	 * Finds instances of a word. 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return instances
	 */
	public static Set<String> findInstances( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.inst);
	}
	/**
	 * Finds has-instance relations of a word.
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return has-instance relations
	 */
	public static Set<String> findHasInstances( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.hasi);
	}
	/**
	 * Get attributes of a word. 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return attributes
	 */
	public static Set<String> findAttributes( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.attr);
	}
	/**
	 * Finds similar-to relations of an adjective(?). 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return similar-to relations
	 */
	public static Set<String> findSimilarTo( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.sim);
	}
	/**
	 * Finds entailed consequents of a word. According to <a href="http://en.wikipedia.org/wiki/WordNet">wikipedia</a>, 
	 * <ul>
	 * <li>(Verb) entailment: the verb Y is entailed by X if by doing X you must be doing Y (to sleep is entailed by to snore)
	 * </ul>
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return consequents
	 */
	public static Set<String> findEntailments( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.enta);
	}
	/**
	 * Finds causes of a word. 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return causes
	 */
	public static Set<String> findCauses( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.caus);
	}

	/**
	 * Finds words in see also relationship.
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return see also words
	 */
	public static Set<String> findSeeAlso( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.also);
	}

	/**
	 * Find synonyms of a word. We assume words sharing the same synsets are synonyms.
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return synonyms
	 */
	public static Set<String> findSynonyms( String word, POS pos ) {
		return WordNetUtil.findSynonyms( word, pos, false );
	}
	
	/**
	 * Find antonyms of a word.
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return antonyms
	 */
	public static Set<String> findAntonyms( String word, POS pos ) {
		return WordNetUtil.findLinks(word, pos, Link.ants);
	}
	
	/**
	 * Get domains of a word. 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return domains
	 */
	public static Set<String> findDomains( String word, POS pos ) {
		Set<String> results = new LinkedHashSet<String>();
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmnc) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmnr) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmnu) );
		return results;
	}
	/**
	 * Get in-domain relations of a word. 
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return in-domain relations
	 */
	public static Set<String> findInDomains( String word, POS pos ) {
		Set<String> results = new LinkedHashSet<String>();
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmtc) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmtr) );
		results.addAll( WordNetUtil.findLinks(word, pos, Link.dmtu) );
		return results;
	}
	
	/**
	 * Finds translations of a word. Basically, synonyms in different language are 
	 * assumed to be translations.
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return translations
	 */
	public static Set<String> findTranslations( String word, POS pos ) {
		return WordNetUtil.findSynonyms( word, pos, true );
	}

	/**
	 * Finds definitions of a word. As of Japanese WordNet version 0.9, only English definitions are available.
	 * 
	 * @param word word in English or Japanese
	 * @param pos part of speech
	 * @return definitions in English
	 */
	public static Set<String> findDefinitions( String word, POS pos ) {
		Set<String> results = new LinkedHashSet<String>();
		List<Synset> synsets = WordNetUtil.wordToSynsets( word, pos );
		// Currently, only English is available.
		Lang lang = Lang.eng; 
		
		for ( Synset synset : synsets ) {
			SynsetDef def = SynsetDefDAO.findSynsetDefBySynsetAndLang( synset.getSynset(), lang );
			results.add( def.getDef() );
		}
		return results;
	}
	
}
