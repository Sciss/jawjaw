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
 * Persistent data class for the "word" table
 * @author Hideki Shima
 *
 */
public class Word implements Cloneable {

	private int wordid;
	private Lang lang;
	private String lemma;
	private String pron;
	private POS pos;
	
	public Word( int wordid, Lang lang, String lemma, String pron, POS pos ) {
		this.wordid = wordid;
		this.lang = lang;
		this.lemma = lemma;
		this.pron = pron;
		this.pos = pos;
	}
	
	/**
	 * Dump the content into String in JSON format
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "{ " );
		sb.append( "\"wordid\":\""+wordid+"\", " );
		sb.append( "\"lang\":\""+lang+"\", " );
		sb.append( "\"lemma\":\""+lemma+"\", " );
		sb.append( "\"pron\":\""+pron+"\", " );
		sb.append( "\"pos\":\""+pos.toString()+"\"" );
		sb.append( " }" );
		return sb.toString();
	}
	
	/**
	 * @return the wordid
	 */
	public int getWordid() {
		return wordid;
	}
	/**
	 * @param wordid the wordId to set
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
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}
	/**
	 * @param lemma the lemma to set
	 */
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	/**
	 * @return the pron
	 */
	public String getPron() {
		return pron;
	}
	/**
	 * @param pron the pron to set
	 */
	public void setPron(String pron) {
		this.pron = pron;
	}
	/**
	 * @return the pos
	 */
	public POS getPos() {
		return pos;
	}
	/**
	 * @param pos the pos to set
	 */
	public void setPos(POS pos) {
		this.pos = pos;
	}
	
	@Override
	public Word clone() {
		return new Word( wordid, Lang.valueOf(lang.toString()), lemma, pron, POS.valueOf(pos.toString()) );
	}
}
