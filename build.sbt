
name := "Frugal Streaming"
version := "0.1.0"
organization := "co.oxfordcomma"
scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.3.0" % "provided" exclude("org.apache.spark", "unused"),
  "org.apache.spark" % "spark-streaming_2.10" % "1.3.0" % "provided",
  "org.scalatest" % "scalatest_2.10" % "2.2.4" % "test"
)

jarName in assembly := "frugal.jar"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", "spark", "unused", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}