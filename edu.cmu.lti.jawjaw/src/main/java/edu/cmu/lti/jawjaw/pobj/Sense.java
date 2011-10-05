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
package edu.cmu.lti.jawjaw.pobj;

/**
 * Persistent data class for the "sense" table
 * @author Hideki Shima
 *
 */
public class Sense implements Cloneable {

	private String synset;
	private int wordid;
	private Lang lang;
	private int rank;
	private int lexid;
	private int freq;
	private String src;
	
	public Sense( String synset, int wordid, Lang lang, 
	int rank, int lexid, int freq, String src ) {
		this.synset = synset;
		this.wordid = wordid;
		this.lang = lang;
		this.rank = rank;
		this.lexid = lexid;
		this.freq = freq;
		this.src = src;
	}

	/**
	 * Dump the content into String in JSON format
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "{ " );
		sb.append( "\"synset\":\""+synset+"\", " );
		sb.append( "\"wordid\":\""+wordid+"\", " );
		sb.append( "\"lang\":\""+lang+"\", " );
		sb.append( "\"rank\":\""+rank+"\", " );
		sb.append( "\"lexid\":\""+lexid+"\", " );
		sb.append( "\"freq\":\""+freq+"\", " );
		sb.append( "\"src\":\""+src+"\"" );
		sb.append( " }" );
		return sb.toString();
	}

	/**
	 * @return the synset
	 */
	public String getSynset() {
		return synset;
	}

	/**
	 * @param synset the synset to set
	 */
	public void setSynset(String synset) {
		this.synset = synset;
	}

	/**
	 * @return the wordid
	 */
	public int getWordid() {
		return wordid;
	}

	/**
	 * @param wordid the wordid to set
	 */
	public void setWordid(int wordid) {
		this.wordid = wordid;
	}

	/**
	 * @return the lang
	 */
	public Lang getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(Lang lang) {
		this.lang = lang;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return the lexid
	 */
	public int getLexid() {
		return lexid;
	}

	/**
	 * @param lexid the lexid to set
	 */
	public void setLexid(int lexid) {
		this.lexid = lexid;
	}

	/**
	 * @return the freq
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * @param freq the freq to set
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * @param src the src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}
	
	@Override
	public Sense clone() {
		return new Sense( synset, wordid, Lang.valueOf(lang.toString()), rank, lexid, freq, src );
	}
	
}
