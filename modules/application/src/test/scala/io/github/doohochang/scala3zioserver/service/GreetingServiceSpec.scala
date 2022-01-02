package io.github.doohochang.scala3zioserver.service

import zio.*
import zio.test.*
import Assertion.*

object GreetingServiceSpec extends DefaultRunnableSpec:

  def spec =
    testM(
      "GreetingServiceImpl should return a valid string for greeting."
    )(
      for
        env <- ZIO.service[GreetingService]
        result1 <- env.greet("Dooho")
        result2 <- env.greet("Dooho Chang")
        result3 <- env.greet("1016")
      yield assert(result1)(equalTo("Hello, Dooho!"))
        && assert(result2)(equalTo("Hello, Dooho Chang!"))
        && assert(result3)(equalTo("Hello, 1016!"))
    ).provideLayer(GreetingServiceImpl.layer)
