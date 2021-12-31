package io.github.doohochang.scala3zioserver.config

import zio.*
import com.typesafe.config.*

/** * Load [[ServerConfig]] from [[com.typesafe.config.Config]]
  */
class ServerConfigLoader(config: Config):
  def load: Task[ServerConfig] =
    for
      serverConfig <- Task { config.getConfig("server") }
      port <- Task { serverConfig.getInt("http.port") }
    yield ServerConfig(port = port)
