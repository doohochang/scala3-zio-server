package io.github.doohochang.scala3zioserver.http

import zio.*

trait Server:
  def run: Task[Unit]
