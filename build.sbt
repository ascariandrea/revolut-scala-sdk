resolvers += "btomala at bintray" at "https://dl.bintray.com/btomala/maven/"
resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven"
resolvers += Resolver.sonatypeRepo("releases")

lazy val commonSettings = Seq(
  organization := "com.ascariandrea.revolut.sdk",
  developers:= List(
    Developer("ascariandrea", "Andrea Ascari", "dev.ascariandrea@gmail.com", url("https://github.com/ascariandrea"))
  ),
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq("2.11.11", scalaVersion.value),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-Xfuture",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  ),
  // Run scalastyle upon compile
  (compile in Compile) := { (compile in Compile) dependsOn (scalastyle in Compile).toTask("") }.value,
  (compile in Test) := { (compile in Test) dependsOn (scalastyle in Test).toTask("") }.value
)

val circeVersion = "0.9.1"

val root = project.in(file("."))
  .settings(
    name := "revolut-sdk",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "com.google.code.findbugs" % "jsr305" % "1.3.9",
      "com.typesafe.akka" %% "akka-http"   % "10.1.0",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11",
      "io.buildo" %% "enumero" % "1.3.0"
    )
    ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
    ++ Seq(
      "org.scalatest" %% "scalatest" % "3.0.5",
      "com.squareup.okhttp3" % "mockwebserver" % "3.10.0",
      "com.danielasfregola" %% "random-data-generator" % "2.4"
    ).map( _ % Test)
  )
  .settings(commonSettings)
