package io.github.doohochang.scala3zioserver

import zio.*
import zio.clock.Clock
import zio.blocking.Blocking

import service.*
import http.service.*
import http.{Server, ServerImpl}

@main def main(): Unit =
  val configLayer = config.layers.serverConfig
  val serviceLayer = GreetingServiceImpl.layer
  val httpServiceLayer = serviceLayer >>> GreetingHttpService.layer
  val serverLayer: TaskLayer[Server] =
    Clock.live ++ Blocking.live ++ httpServiceLayer ++ configLayer >>> ServerImpl.layer

  val runServer =
    (for
      server <- ZIO.environment[Server]
      _ <- server.run
    yield ())
      .provideLayer(serverLayer)

  val exitResult = Runtime.default.unsafeRunSync(runServer)
  println(exitResult)
