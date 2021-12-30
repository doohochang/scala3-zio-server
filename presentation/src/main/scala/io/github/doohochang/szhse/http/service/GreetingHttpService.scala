package io.github.doohochang.szhse.http.service

import cats.effect.*
import zio.*
import zio.interop.catz.*
import org.http4s.*
import org.http4s.dsl.Http4sDslBinCompat

import io.github.doohochang.szhse.service.GreetingService

class GreetingHttpService(service: GreetingService):
  object Dsl extends Http4sDslBinCompat[Task]
  import Dsl.*

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "greet" / name =>
      for
        greeting <- service.greet(name)
        response <- Ok(greeting)
      yield response
  }

object GreetingHttpService:
  val layer: URLayer[GreetingService, Has[GreetingHttpService]] =
    ZLayer.fromFunction(GreetingHttpService(_))
