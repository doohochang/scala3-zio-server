package io.github.doohochang.scala3zioserver.service

import zio.*

class GreetingServiceImpl extends GreetingService:
  def greet(name: String): UIO[String] =
    UIO.succeed(s"Hello, $name!")

object GreetingServiceImpl:
  val layer: ULayer[Has[GreetingService]] =
    ZLayer.succeed(GreetingServiceImpl())
