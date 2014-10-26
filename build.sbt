import sbt._
import Keys._


name := "Lifecycle-scala-sbt-project-sample"

version := "1.0"

scalaVersion := "2.11.1"

packageBin <<= packageBin in Compile dependsOn BuildApp.weaveClassImpl

sbt.Keys.`package` in Compile <<= sbt.Keys.`package` in Compile dependsOn BuildApp.weaveClassImpl

test <<= test in Test dependsOn BuildApp.weaveClassImpl

libraryDependencies += "junit" % "junit" % "4.9" % Test

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
