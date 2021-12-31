package io.github.doohochang.scala3zioserver.http

import cats.effect.*
import zio.{ExitCode as _, *}
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz.*
import org.http4s.blaze.server.*
import org.http4s.server.Router
import org.http4s.server.middleware.Logger

import service.*

class ServerImpl(greetingService: GreetingHttpService)(using
    Runtime[Clock with Blocking]
) extends Server:
  private val build: BlazeServerBuilder[Task] =
    BlazeServerBuilder[Task]
      .bindHttp(port = 8080, host = "0.0.0.0")
      .withHttpApp(
        Logger.httpApp[Task](logHeaders = true, logBody = true)(
          Router[Task]("/" -> greetingService.routes).orNotFound
        )
      )

  def run: Task[Unit] =
    build.serve.compile[Task, Task, ExitCode].drain

object ServerImpl:
  val layer
      : URLayer[Has[GreetingHttpService] with Clock with Blocking, Server] =
    ZLayer.fromEffectMany(
      for
        greetingService <- ZIO.service[GreetingHttpService]
        runtime <- ZIO.runtime[Clock with Blocking]
      yield ServerImpl(greetingService)(using runtime)
    )
