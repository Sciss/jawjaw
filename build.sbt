name              := "jawjaw"

organization      := "de.sciss"

version           := "0.1.0"

scalaVersion      := "2.11.7"

licenses          := Seq("Apache License v2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))

crossPaths        := false

autoScalaLibrary  := false

libraryDependencies ++= Seq(
  "org.xerial"    % "sqlite-jdbc"     % "3.8.11.2",
  "com.novocode"  % "junit-interface" % "0.11"     % "test"
)

// cf. http://www.scala-sbt.org/0.13.5/docs/Detailed-Topics/Classpaths.html
unmanagedClasspath in Runtime += baseDirectory.value / "config"
unmanagedClasspath in Test    += baseDirectory.value / "config"

homepage          := Some(url(s"https://github.com/Sciss/${name.value}"))

description       := "WordNet Similarity for Java provides an API for several Semantic Relatedness/Similarity algorithms"

lazy val commonJavaOptions = Seq("-source", "1.6")

javacOptions        := commonJavaOptions ++ Seq("-target", "1.6", "-g", "-Xlint:deprecation")

javacOptions in doc := commonJavaOptions  // cf. sbt issue #355

mainClass in Compile := Some("edu.cmu.lti.jawjaw.demo.AdvancedAPIDemo")

// ---- publishing to Maven Central ----
publishMavenStyle := true

publishTo := Some(
  if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
)

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
  <scm>
    <url>git@github.com:Sciss/{n}.git</url>
    <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
  </scm>
  <developers>
    <developer>
      <id>hideki.shima</id>
      <name>Hideki Shima</name>
      <url>http://www.cs.cmu.edu/~hideki</url>
    </developer>
    <developer>
      <id>sciss</id>
      <name>Hanns Holger Rutz</name>
      <url>http://www.sciss.de</url>
    </developer>
  </developers>
}
