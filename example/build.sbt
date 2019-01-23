resolvers += "ascariandrea at bintray" at "https://dl.bintray.com/ascariandrea/maven"

lazy val commonSettings = Seq(
  name := "revolut-sdk-example",
  organization := "com.ascariandrea",
  developers := List(
    Developer("ascariandrea",
              "Andrea Ascari",
              "dev.ascariandrea@gmail.com",
              url("https://github.com/ascariandrea"))
  ),
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.11", scalaVersion.value),
  scalacOptions ++= Seq(
    "-deprecation", "-feature", "-unchecked", "-Xlint",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  )
)

val root = project
  .in(file("."))
  .settings(
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq("com.ascariandrea" %% "revolut" % "0.1.1")
      ++ Seq(
      "org.scalatest" %% "scalatest" % "3.0.5",
      "com.squareup.okhttp3" % "mockwebserver" % "3.10.0",
      "com.danielasfregola" %% "random-data-generator" % "2.4"
    ).map(_ % Test)
  )
  .settings(commonSettings)

fork in run := true
cancelable in Global := true
