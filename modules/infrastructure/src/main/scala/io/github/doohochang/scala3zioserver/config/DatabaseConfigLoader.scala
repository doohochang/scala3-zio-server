package io.github.doohochang.scala3zioserver.config

import zio.*
import com.typesafe.config.*

class DatabaseConfigLoader(config: Config):
  def load: Task[DatabaseConfig] =
    for
      db <- Task { config.getConfig("db") }
      host <- Task { db.getString("host") }
      port <- Task { db.getInt("port") }
      name <- Task { db.getString("name") }
      user <- Task { db.getString("user") }
      password <- Task { db.getString("password") }
    yield DatabaseConfig(host, port, name, user, password)
