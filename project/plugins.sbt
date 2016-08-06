logLevel := Level.Warn

val jsdocgenVersion = "0.1.3-SNAPSHOT"



//lazy val root = (project in file("."))
//  .dependsOn(jsdocgenPlugin)

//lazy val jsdocgenPlugin = ProjectRef(uri("../../scalajs-jsdocgen"), "plugin")

val repo = "http://localhost:38084"
val snapshots = "snapshots" at s"$repo/snapshots"
val releases = "releases" at s"$repo/releases"


resolvers ++= Seq(
  snapshots,
  releases
)

//resolvers ++= Seq(
//  Resolver.defaultLocal,
//  sbtglobal.SbtGlobals.devops,
//  Resolver.sonatypeRepo("snapshots")
//)

addSbtPlugin("com.github.maprohu" % "jsdocgen-plugin" % jsdocgenVersion)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")