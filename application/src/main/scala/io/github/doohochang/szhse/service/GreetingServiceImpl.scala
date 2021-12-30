package io.github.doohochang.szhse.service

import zio.*

class GreetingServiceImpl extends GreetingService:
  def greet(name: String): UIO[String] =
    UIO.succeed(s"Hello, $name!")

object GreetingServiceImpl:
  val layer: ULayer[GreetingService] =
    ZLayer.succeedMany(GreetingServiceImpl())
