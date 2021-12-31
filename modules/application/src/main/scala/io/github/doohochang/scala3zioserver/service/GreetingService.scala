package io.github.doohochang.scala3zioserver.service

import zio.*

trait GreetingService:
  def greet(name: String): UIO[String]
