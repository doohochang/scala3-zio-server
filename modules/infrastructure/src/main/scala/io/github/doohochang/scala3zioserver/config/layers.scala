package io.github.doohochang.scala3zioserver.config

import com.typesafe.config.*
import zio.*

val rootConfigLayer: TaskLayer[Has[Config]] =
  ZLayer.fromEffect(Task { ConfigFactory.load() })

val serverConfigLayer: RLayer[Has[Config], Has[ServerConfig]] =
  ZLayer.fromEffect(loadServerConfig)

val databaseConfigLayer: RLayer[Has[Config], Has[DatabaseConfig]] =
  ZLayer.fromEffect(loadDatabaseConfig)

private def loadServerConfig: RIO[Has[Config], ServerConfig] =
  for
    root <- ZIO.service[Config]
    server <- Task { root.getConfig("server") }
    port <- Task { server.getInt("http.port") }
  yield ServerConfig(port = port)

private def loadDatabaseConfig: RIO[Has[Config], DatabaseConfig] =
  for
    root <- ZIO.service[Config]
    db <- Task { root.getConfig("db") }
    host <- Task { db.getString("host") }
    port <- Task { db.getInt("port") }
    name <- Task { db.getString("name") }
    user <- Task { db.getString("user") }
    password <- Task { db.getString("password") }
  yield DatabaseConfig(host, port, name, user, password)
