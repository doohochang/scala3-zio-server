package io.github.doohochang.szhse.service

import zio.*

trait GreetingService:
  def greet(name: String): UIO[String]
