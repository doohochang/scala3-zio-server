package io.github.doohochang.scala3zioserver
package http

import cats.effect.*
import org.http4s.*
import zio.*
import zio.interop.catz.*

import service.GreetingService

class GreetingHttpRoutes(service: GreetingService):
  import http4sDsl.*

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "greet" / name =>
      for
        greeting <- service.greet(name)
        response <- Ok(greeting)
      yield response
  }

object GreetingHttpRoutes:
  val layer: URLayer[Has[GreetingService], Has[GreetingHttpRoutes]] =
    ZLayer.fromEffect(
      for greetingService <- ZIO.service[GreetingService]
      yield GreetingHttpRoutes(greetingService)
    )
