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

val root = project.in(file("."))
  .settings(
    name := "revolut-sdk",
    libraryDependencies ++= Seq(
      "com.google.code.findbugs" % "jsr305" % "1.3.9",
      "com.squareup.okhttp3" % "okhttp" % "3.9.1",
      "io.spray" %% "spray-json" % "1.3.2"
    ) ++ Seq(
      "org.scalatest" %% "scalatest" % "3.0.5",
      "com.squareup.okhttp3" % "mockwebserver" % "3.10.0"
    ).map( _ % Test)
  )
  .settings(commonSettings)
