logLevel := Level.Warn


lazy val root = (project in file("."))
  .dependsOn(jsdocgenPlugin)

lazy val jsdocgenPlugin = ProjectRef(uri("../../scalajs-jsdocgen"), "plugin")

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("com.github.maprohu" % "jsdocgen-plugin" % "0.1.1-SNAPSHOT")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")