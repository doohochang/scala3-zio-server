import sbt._

object Dependencies {
  private val zio = Seq(
    "dev.zio" %% "zio" % Versions.zio,
    "dev.zio" %% "zio-streams" % Versions.zio,
    "dev.zio" %% "zio-interop-cats" % Versions.zioCatsInterop
  )
  private val zioTest = Seq(
    "dev.zio" %% "zio-test" % Versions.zio % "test",
    "dev.zio" %% "zio-test-sbt" % Versions.zio % "test"
  )

  private val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % Versions.http4s,
    "org.http4s" %% "http4s-blaze-server" % Versions.http4s,
    "org.http4s" %% "http4s-blaze-client" % Versions.http4s
  )

  private val cats = Seq(
    "org.typelevel" %% "cats-core" % Versions.catsCore,
    "org.typelevel" %% "cats-effect" % Versions.catsEffect
  )

  private val logback = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logback,
    "ch.qos.logback" % "logback-core" % Versions.logback,
    "org.slf4j" % "slf4j-api" % Versions.slf4j
  )

  val common = zio ++ zioTest ++ cats

  val presentation = http4s ++ logback
}
