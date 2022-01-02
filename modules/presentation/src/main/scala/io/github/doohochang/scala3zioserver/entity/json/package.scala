package io.github.doohochang.scala3zioserver
package entity

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

package object json:
  given JsonEncoder[Article] = DeriveJsonEncoder.gen[Article]
  given JsonDecoder[Article] = DeriveJsonDecoder.gen[Article]
