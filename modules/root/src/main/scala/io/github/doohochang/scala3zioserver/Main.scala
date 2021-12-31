package io.github.doohochang.scala3zioserver

import zio.*
import zio.clock.Clock
import zio.blocking.Blocking

import service.*
import http.service.*
import http.{Server, ServerImpl}

@main def hello: Unit =
  val serviceLayer = GreetingServiceImpl.layer
  val httpServiceLayer = serviceLayer >>> GreetingHttpService.layer
  val serverLayer: ULayer[Server] =
    Clock.live ++ Blocking.live ++ httpServiceLayer >>> ServerImpl.layer

  val runServer =
    (for
      server <- ZIO.environment[Server]
      _ <- server.run
    yield ())
      .provideLayer(serverLayer)

  val exitResult = Runtime.default.unsafeRunSync(runServer)
  println(exitResult)
