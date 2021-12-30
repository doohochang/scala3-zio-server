package io.github.doohochang.szhse.http

import zio.*

trait Server:
  def run: Task[Unit]
