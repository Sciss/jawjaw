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
 * Enum class for supported lexical relationships.
 * Note that not all are supported in the current version of the DB
 * @author Hideki Shima
 *
 */
public enum Link {
	// Comments include description and the number of instances found in v0.9
	
	also, //See also　2692
	syns, //Synonyms 0
	hype, //Hypernyms 89089
	inst, //Instances 8577
	hypo, //Hyponym   89089
	hasi, //Has Instance 8577
	mero, //Meronyms 0
	mmem, //Meronyms --- Member 12293
	msub, //Meronyms --- Substance 979
	mprt, //Meronyms --- Part 9097
	holo, //Holonyms 0
	hmem, //Holonyms --- Member 12293
	hsub, //Holonyms --- Substance 797
	hprt, //Holonyms -- Part 9097
	attr, //Attributes 1278
	sim,  //Similar to 21386
	enta, //Entails 408
	caus, //Causes 220
	dmnc, //Domain --- Category 6643
	dmnu, //Domain --- Usage 967
	dmnr, //Domain --- Region 1345
	dmtc, //In Domain --- Category 6643
	dmtu, //In Domain --- Usage 967
	dmtr, //In Domain --- Region 1345
	ants, //Antonyms 0
}
