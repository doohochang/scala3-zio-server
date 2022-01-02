package io.github.doohochang.scala3zioserver
package http

import cats.effect.*
import cats.implicits.*
import zio.{ExitCode as _, *}
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz.*
import org.http4s.blaze.server.*
import org.http4s.server.Router
import org.http4s.server.middleware.Logger

import config.ServerConfig

class ServerImpl(
    greetingService: GreetingHttpRoutes,
    articleService: ArticleHttpRoutes,
    config: ServerConfig
)(using
    Runtime[Clock with Blocking]
) extends Server:
  private val build: BlazeServerBuilder[Task] =
    BlazeServerBuilder[Task]
      .bindHttp(port = config.port, host = "0.0.0.0")
      .withHttpApp(
        Logger.httpApp[Task](logHeaders = true, logBody = true)(
          Router[Task](
            "/" -> (greetingService.routes <+> articleService.routes)
          ).orNotFound
        )
      )

  def run: Task[Unit] =
    build.serve.compile[Task, Task, ExitCode].drain

object ServerImpl:
  type Deps = Has[GreetingHttpRoutes]
    with Has[ArticleHttpRoutes]
    with Has[ServerConfig]
    with Clock
    with Blocking

  val layer: URLayer[Deps, Has[Server]] =
    ZLayer.fromEffect(
      for
        greetingService <- ZIO.service[GreetingHttpRoutes]
        articleService <- ZIO.service[ArticleHttpRoutes]
        config <- ZIO.service[ServerConfig]
        runtime <- ZIO.runtime[Clock with Blocking]
      yield ServerImpl(greetingService, articleService, config)(using runtime)
    )
