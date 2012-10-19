Thank you for downloading JAWJAW (http://code.google.com/p/jawjaw).

This software provides API for the NICT Japanese WordNet
(http://nlpwww.nict.go.jp/wn-ja/index.en.html)

---- Preparation ----
Before using JAWJAW, make sure to download and put the NICT WordNet DB file 
under the data directory (i.e. src/main/resources/wnjpn-0.9.db), 
and specify the correct name in the config file at 
src/main/config/jawjaw.conf

---- Testing ----
You can verify that the preparation is correctly done by running
JUnit test cases. 

Test cases:
  src/test/*
   
Maven command:
  mvn test

Launch file for Eclipse + m2eclipse:
  launches/JAWJAW_JUnitTestAll.launch

---- Using JAWJAW ----
See working examples in the following files.

Demos:
  src/main/java/edu/cmu/lti/jawjaw/demo/SimpleAPIDemo.java
  src/main/java/edu/cmu/lti/jawjaw/demo/AdvancedAPIDemo.java

Test cases:
  src/test/*

---- Packaging ----
(A) Create a jar file including wordnet db and depended jar files
(recommended for using JAWJAW from other Java projects)

Maven command: 
  mvn package assembly:single

Launch file for eclipse + m2eclipse: 
  launches/JAWJAW_package_with_dependency.launch

Output jar file (may need a refresh on the directory):
  target/jawjaw-jar-with-dependencies.jar


(B) Create a jar file including wordnet db but NOT including depended jar files
(recommended for using JAWJAW with WS4J)

Maven command: 
  mvn install

Launch file for eclipse + m2eclipse: 
  launches/JAWJAW_install_without_dependency.launch

Output jar file (may need a refresh on the directory):
  target/jawjaw.jar

---- Related Project ----
Please also see our sister project WS4J (WordNet Similarity for Java).
http://code.google.com/p/ws4j/
