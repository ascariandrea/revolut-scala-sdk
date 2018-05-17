enablePlugins(GitVersioning)

resolvers += "btomala at bintray" at "https://dl.bintray.com/btomala/maven/"
resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven"
resolvers += Resolver.sonatypeRepo("releases")

lazy val commonSettings = Seq(
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
  (compile in Compile) := {
    (compile in Compile) dependsOn (scalastyle in Compile).toTask("")
  }.value,
  (compile in Test) := {
    (compile in Test) dependsOn (scalastyle in Test).toTask("")
  }.value
)

val circeVersion = "0.9.3"

val root = project
  .in(file("."))
  .settings(
    organization := "com.ascariandrea",
    name := "revolut",
    bintrayOrganization := Some("ascariandrea"),
    bintrayVcsUrl := Some("git@github.com:ascariandrea/revolut-sdk-scala"),
    licenses := Seq(
      "MIT" -> url("http://www.opensource.org/licenses/mit-license.html")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ascariandrea/revolut-sdk-scala"),
        "scm:git:git@github.com:ascariandrea/revout-sdk-scala.git"
      )
    ),
    pomExtra :=
      <developers>
        <developer>
          <id>ascariandrea</id>
          <name>Andrea Ascari</name>
          <url>github.com/ascariandrea</url>
        </developer>
      </developers>,
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "com.google.code.findbugs" % "jsr305" % "1.3.9",
      "com.typesafe.akka" %% "akka-http" % "10.1.0",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11",
      "io.buildo" %% "enumero" % "1.3.0",
      "io.buildo" %% "enumero-circe-support" % "1.3.0"
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
      ).map(_ % Test)
  )
  .settings(commonSettings)
