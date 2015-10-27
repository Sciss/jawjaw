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

import edu.cmu.lti.jawjaw.db.SynsetDAO;

/**
 * Persistent data class for the "synset" table
 * @author Hideki Shima
 *
 */

public class Synset implements Cloneable {

	private String synset;
	private POS pos;
	private String name;
	private String src;
	
	public Synset( String synset ) {
		this.synset = synset; 
	}
	
	public Synset( String synset, POS pos, String name, String src ) {
		this.synset = synset;
		this.pos = pos;
		this.name = name;
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
		sb.append( "\"pos\":\""+pos+"\", " );
		sb.append( "\"name\":\""+name+"\", " );
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
	 * @return the pos
	 */
	public POS getPos() {
		if ( pos==null ) {
			Synset realSynset = SynsetDAO.findSynsetBySynset( getSynset() );
			setName( realSynset.getName() );
			setPos( realSynset.getPos() );
			setSrc( realSynset.getSrc() );
		}
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(POS pos) {
		this.pos = pos;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		if ( name==null ) {
			Synset realSynset = SynsetDAO.findSynsetBySynset( getSynset() );
			setName( realSynset.getName() );
			setPos( realSynset.getPos() );
			setSrc( realSynset.getSrc() );
			return name;
		}
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	public Synset clone() {
		return new Synset( synset, POS.valueOf(pos.toString()), name, src );
	}
}
