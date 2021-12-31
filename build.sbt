ThisBuild / organization := "io.github.doohochang"
ThisBuild / scalaVersion := Versions.scala3
ThisBuild / version := Versions.build
ThisBuild / libraryDependencies ++= Dependencies.common
ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

lazy val infrastructure = project
  .in(file("infrastructure"))
  .dependsOn(domain, application)

lazy val domain = project
  .in(file("domain"))

lazy val application = project
  .in(file("application"))
  .dependsOn(domain)

lazy val presentation = project
  .in(file("presentation"))
  .dependsOn(application)
  .settings(
    libraryDependencies ++= Dependencies.presentation
  )

lazy val root = project
  .in(file("."))
  .dependsOn(infrastructure, domain, application, presentation)
  .aggregate(infrastructure, domain, application, presentation)
  .settings(
    name := "scala3-zio-server"
  )

enablePlugins(JavaAppPackaging, DockerPlugin)

dockerBaseImage := "adoptopenjdk/openjdk11"
Docker / packageName := "scala3-zio-server"
Docker / version := Versions.build
dockerUpdateLatest := true
