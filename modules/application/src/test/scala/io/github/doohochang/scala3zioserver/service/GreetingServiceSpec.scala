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
        result1 <- env.greet("Alice")
        result2 <- env.greet("Bob White")
        result3 <- env.greet("1016")
      yield assert(result1)(equalTo("Hello, Alice!"))
        && assert(result2)(equalTo("Hello, Bob White!"))
        && assert(result3)(equalTo("Hello, 1016!"))
    ).provideLayer(GreetingServiceImpl.layer)
