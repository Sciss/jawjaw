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
 * Persistent data class for the "synset_def" table
 * @author Hideki Shima
 *
 */
public class SynsetDef implements Cloneable {

	private String synset;
	private Lang lang;
	private String def;
	private int sid;
	
	public SynsetDef( String synset, Lang lang, String def, int sid ) {
		this.synset = synset;
		this.lang = lang;
		this.def = def;
		this.sid = sid;
	}

	/**
	 * Dump the content into String in JSON format
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "{ " );
		sb.append( "\"synset\":\""+synset+"\", " );
		sb.append( "\"lang\":\""+lang+"\", " );
		sb.append( "\"def\":\""+def+"\", " );
		sb.append( "\"sid\":\""+sid+"\"" );
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
	 * @return the def
	 */
	public String getDef() {
		return def;
	}

	/**
	 * @param def the def to set
	 */
	public void setDef(String def) {
		this.def = def;
	}

	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	@Override
	public SynsetDef clone() {
		return new SynsetDef( synset, Lang.valueOf(lang.toString()), def, sid );
	}
	
}
