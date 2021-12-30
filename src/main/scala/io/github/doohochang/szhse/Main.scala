package io.github.doohochang.szhse

import service.*
import http.service.*

@main def hello: Unit =
  val serviceLayer = GreetingServiceImpl.layer
//  val httpServiceLayer = GreetingHttpService.layer
//  val appLayer = serviceLayer >>> httpServiceLayer

  println("Hello world!")
