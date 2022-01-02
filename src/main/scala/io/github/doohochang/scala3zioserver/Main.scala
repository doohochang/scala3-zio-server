package io.github.doohochang.scala3zioserver

import zio.*
import zio.clock.Clock
import zio.blocking.Blocking
import config.*
import repository.*
import service.*
import http.*

type RuntimeDeps = Clock with Blocking
type Configs = Has[ServerConfig] with Has[DatabaseConfig]
type Repositories = Has[ArticleRepository]
type Services = Has[GreetingService] with Has[ArticleService]
type HttpRoutes = Has[GreetingHttpRoutes] with Has[ArticleHttpRoutes]

@main def main(): Unit =
  val runtimeLayer: ULayer[RuntimeDeps] = Clock.live ++ Blocking.live

  val configLayer: TaskLayer[Configs] =
    rootConfigLayer >>> (serverConfigLayer ++ databaseConfigLayer)

  val repositoryLayer: RLayer[Configs with RuntimeDeps, Repositories] =
    (
      ZLayer.identity[Configs with RuntimeDeps]
        >+> repository.transactorLayer
    ) >>> ArticleRepositoryImpl.layer

  val serviceLayer: URLayer[Repositories, Services] =
    GreetingServiceImpl.layer ++ ArticleServiceImpl.layer

  val httpRoutesLayer: URLayer[Services, HttpRoutes] =
    GreetingHttpRoutes.layer ++ ArticleHttpRoutes.layer

  val serverLayer: TaskLayer[Has[Server]] =
    (runtimeLayer ++ configLayer)
      >+> repositoryLayer
      >+> serviceLayer
      >+> httpRoutesLayer
      >>> ServerImpl.layer

  val runServer =
    (for
      server <- ZIO.service[Server]
      _ <- server.run
    yield ())
      .provideLayer(serverLayer)

  val exitResult = Runtime.default.unsafeRunSync(runServer)
  println(exitResult)
