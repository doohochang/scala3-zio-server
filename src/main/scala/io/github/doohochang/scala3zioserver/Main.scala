package io.github.doohochang.scala3zioserver

import zio.*
import zio.clock.Clock
import zio.blocking.Blocking
import config.*
import repository.*
import service.*
import http.service.{ArticleHttpService, *}
import http.{Server, ServerImpl}

type RuntimeDeps = Clock with Blocking
type ConfigDeps = Has[ServerConfig] with Has[DatabaseConfig]
type RepositoryDeps = Has[ArticleRepository]
type ServiceDeps = Has[GreetingService] with Has[ArticleService]
type HttpServiceDeps = Has[GreetingHttpService] with Has[ArticleHttpService]
type ServerDeps = Has[Server]

@main def main(): Unit =
  val runtimeLayer: ULayer[RuntimeDeps] = Clock.live ++ Blocking.live

  val configLayer: TaskLayer[ConfigDeps] =
    rootConfigLayer >>> (serverConfigLayer ++ databaseConfigLayer)

  val repositoryLayer: RLayer[ConfigDeps with RuntimeDeps, RepositoryDeps] =
    (
      ZLayer.identity[ConfigDeps with RuntimeDeps]
        >+> repository.transactorLayer
    ) >>> ArticleRepositoryImpl.layer

  val serviceLayer: URLayer[RepositoryDeps, ServiceDeps] =
    GreetingServiceImpl.layer ++ ArticleServiceImpl.layer

  val httpServiceLayer: URLayer[ServiceDeps, HttpServiceDeps] =
    GreetingHttpService.layer ++ ArticleHttpService.layer

  val serverLayer: TaskLayer[Has[Server]] =
    (runtimeLayer ++ configLayer)
      >+> repositoryLayer
      >+> serviceLayer
      >+> httpServiceLayer
      >>> ServerImpl.layer

  val runServer =
    (for
      server <- ZIO.service[Server]
      _ <- server.run
    yield ())
      .provideLayer(serverLayer)

  val exitResult = Runtime.default.unsafeRunSync(runServer)
  println(exitResult)
