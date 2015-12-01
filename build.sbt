val githubRepo = "scalajs-o3d"

val commonSettings = Seq(
  organization := "com.github.maprohu",
  version := "0.1.1-SNAPSHOT",
  resolvers += Resolver.sonatypeRepo("snapshots"),

  scalaVersion := "2.11.7",
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },

  pomIncludeRepository := { _ => false },
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  homepage := Some(url(s"https://github.com/maprohu/${githubRepo}")),
  pomExtra := (
      <scm>
        <url>git@github.com:maprohu/{githubRepo}.git</url>
        <connection>scm:git:git@github.com:maprohu/{githubRepo}.git</connection>
      </scm>
      <developers>
        <developer>
          <id>maprohu</id>
          <name>maprohu</name>
          <url>https://github.com/maprohu</url>
        </developer>
      </developers>
    )
)

val noPublish = Seq(
  publishArtifact := false,
  publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
)

//lazy val jsdocgenLib = ProjectRef(uri("../scalajs-jsdocgen"), "lib")

lazy val facade = project
  .settings(commonSettings)
//  .dependsOn(jsdocgenLib)
  .enablePlugins(JsdocPlugin, ScalaJSPlugin)
  .settings(
    publishArtifact in (Compile, packageDoc) := false,
    name := githubRepo,
    jsdocRunSource := Some(
      ((sourceDirectory in Compile).value / "javascript").toURI
    ),
    jsdocRunInputs := Seq("o3d-webgl", "o3djs"),
    jsdocRunTarget := target.value / "o3d-jsdoc.json",
//    jsdocDocletsFile := target.value / "o3d-jsdoc.json",
    jsdocGlobalScope := Seq("o3dfacade"),
    jsdocUtilScope := "pkg",
    sourceGenerators in Compile += jsdocGenerate.taskValue,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0"
    ),
    mappings in (Compile, packageSrc) ++=
      (managedSources in Compile).value pair relativeTo((sourceManaged in Compile).value)

  )

lazy val testapp = project
  .settings(commonSettings)
  .settings(noPublish)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(facade)
  .settings(
    persistLauncher in Compile := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0",
      "com.lihaoyi" %%% "scalatags" % "0.5.3"
    )

  )

lazy val root = (project in file("."))
  .settings(noPublish)
  .aggregate(facade, testapp)