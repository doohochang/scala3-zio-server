package io.github.doohochang.scala3zioserver.config

case class DatabaseConfig(
    host: String,
    port: Int,
    databaseName: String,
    user: String,
    password: String
)
