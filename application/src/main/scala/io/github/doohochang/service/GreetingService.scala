package io.github.doohochang.service

import zio.*

class GreetingService:
  def greet(name: String): ZIO[Any, Nothing, String] =
    UIO.succeed(s"Hello, $name!")

object GreetingServiceImpl:
  lazy val layer: ULayer[GreetingService] =
    ZLayer.succeedMany(GreetingService())
