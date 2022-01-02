package io.github.doohochang.scala3zioserver

import zio.json.*

import entity.*

package object json:
  given JsonEncoder[Article] = DeriveJsonEncoder.gen[Article]
  given JsonDecoder[Article] = DeriveJsonDecoder.gen[Article]
