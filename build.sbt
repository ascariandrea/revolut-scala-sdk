lazy val commonSettings = Seq(
  organization := "com.ascariandrea.revolut.sdk",
  developers:= List(
    Developer("ascariandrea", "Andrea Ascari", "dev.ascariandrea@gmail.com", url("https://github.com/ascariandrea"))
  ),
  scalaVersion := "2.12.4",
  crossScalaVersions := Seq(scalaVersion.value, "2.11.11"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-language:implicitConversions",
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

resolvers += "btomala at bintray" at "https://dl.bintray.com/btomala/maven/"

val root = project.in(file("."))
  .settings(
    name := "revolut-sdk",
    libraryDependencies ++= Seq(
      "com.google.code.findbugs" % "jsr305" % "1.3.9",
      "com.typesafe.akka" %% "akka-http"   % "10.1.0",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11"
    )
    ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
    ++ Seq(
      "org.scalatest" %% "scalatest" % "3.0.5",
      "com.squareup.okhttp3" % "mockwebserver" % "3.10.0"
    ).map( _ % Test)
  )
  .settings(commonSettings)
