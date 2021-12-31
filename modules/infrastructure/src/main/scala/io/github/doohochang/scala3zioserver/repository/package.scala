package io.github.doohochang.scala3zioserver

import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor
import cats.effect.*
import zio.*
import zio.clock.Clock
import zio.blocking.Blocking
import zio.interop.catz.*

import config.DatabaseConfig

package object repository:
  val transactorLayer: URLayer[Has[DatabaseConfig]
    with Runtime[Clock with Blocking], Transactor[Task]] =
    ZLayer.fromEffectMany(
      for
        config <- ZIO.service[DatabaseConfig]
        runtime <- ZIO.environment[Runtime[Clock with Blocking]]
      yield {
        given Runtime[Clock with Blocking] = runtime

        Transactor.fromDriverManager[Task](
          driver = "org.postgresql.Driver",
          url =
            s"jdbc:postgresql://${config.host}:${config.port}/${config.databaseName}",
          user = config.user,
          pass = config.password
        )
      }
    )
