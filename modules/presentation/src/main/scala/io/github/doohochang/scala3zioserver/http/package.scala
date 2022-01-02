package io.github.doohochang.scala3zioserver

import zio.Task
import org.http4s.dsl.Http4sDslBinCompat

package object http:
  object http4sDsl extends Http4sDslBinCompat[Task]
