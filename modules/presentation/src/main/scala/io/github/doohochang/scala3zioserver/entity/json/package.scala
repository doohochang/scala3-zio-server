package io.github.doohochang.scala3zioserver.entity

import zio.json.*

package object json:
  given JsonEncoder[Article] = DeriveJsonEncoder.gen[Article]
  given JsonDecoder[Article] = DeriveJsonDecoder.gen[Article]
