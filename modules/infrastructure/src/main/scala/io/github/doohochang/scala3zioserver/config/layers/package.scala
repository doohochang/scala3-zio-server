package io.github.doohochang.scala3zioserver.config

import com.typesafe.config.*
import zio.*

package object layers:
  val defaultConfig: TaskLayer[Has[Config]] =
    ZLayer.fromEffect(Task { ConfigFactory.load() })

  val serverConfig: TaskLayer[Has[ServerConfig]] =
    defaultConfig >>> ZLayer.fromEffect(
      for
        config <- ZIO.service[Config]
        serverConfig <- ServerConfigLoader(config).load
      yield serverConfig
    )
