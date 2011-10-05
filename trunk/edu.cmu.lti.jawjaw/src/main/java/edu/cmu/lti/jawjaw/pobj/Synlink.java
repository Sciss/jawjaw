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
 * Persistent data class for the "synlink" table
 * @author Hideki Shima
 *
 */
public class Synlink implements Cloneable {

	private String synset1;
	private String synset2;
	private Link link;
	private String src;
	
	public Synlink( String synset1, String synset2, Link link, String src ) {
		this.synset1 = synset1;
		this.synset2 = synset2;
		this.link = link;
		this.src = src;
	}

	/**
	 * Dump the content into String in JSON format
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "{ " );
		sb.append( "\"synset1\":\""+synset1+"\", " );
		sb.append( "\"synset2\":\""+synset2+"\", " );
		sb.append( "\"link\":\""+link+"\", " );
		sb.append( "\"src\":\""+src+"\"" );
		sb.append( " }" );
		return sb.toString();
	}
	
	/**
	 * @return the synset1
	 */
	public String getSynset1() {
		return synset1;
	}

	/**
	 * @param synset1 the synset1 to set
	 */
	public void setSynset1(String synset1) {
		this.synset1 = synset1;
	}

	/**
	 * @return the synset2
	 */
	public String getSynset2() {
		return synset2;
	}

	/**
	 * @param synset2 the synset2 to set
	 */
	public void setSynset2(String synset2) {
		this.synset2 = synset2;
	}

	/**
	 * @return the link
	 */
	public Link getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(Link link) {
		this.link = link;
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
	public Synlink clone() {
		return new Synlink( synset1, synset2, Link.valueOf(link.toString()), src );
	}
	
}
