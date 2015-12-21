logLevel := Level.Warn

val jsdocgenVersion = "0.1.2"



//lazy val root = (project in file("."))
//  .dependsOn(jsdocgenPlugin)

//lazy val jsdocgenPlugin = ProjectRef(uri("../../scalajs-jsdocgen"), "plugin")

resolvers ++= Seq(
  Resolver.defaultLocal,
  sbtglobal.SbtGlobals.devops,
  Resolver.sonatypeRepo("snapshots")
)

addSbtPlugin("com.github.maprohu" % "jsdocgen-plugin" % jsdocgenVersion)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")