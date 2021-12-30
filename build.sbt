ThisBuild / organization := "io.github.doohochang"
ThisBuild / scalaVersion := Versions.scala3
ThisBuild / version := Versions.build

enablePlugins(JavaAppPackaging, DockerPlugin)

dockerBaseImage := "adoptopenjdk/openjdk11"
Docker / packageName := "scala3-zio-http4s-server-example"
Docker / version := Versions.build

lazy val infrastructure = project
  .in(file("infrastructure"))
  .dependsOn(domain, application)
  .settings(
    libraryDependencies ++= Dependencies.common
  )

lazy val domain = project
  .in(file("domain"))
  .settings(
    libraryDependencies ++= Dependencies.common
  )

lazy val application = project
  .in(file("application"))
  .dependsOn(domain)
  .settings(
    libraryDependencies ++= Dependencies.common
  )

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
    name := "scala3-zio-http4s-server-example",
    libraryDependencies ++= Dependencies.common
  )
