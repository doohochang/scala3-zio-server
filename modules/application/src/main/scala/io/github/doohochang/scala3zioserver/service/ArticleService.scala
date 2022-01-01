package io.github.doohochang.scala3zioserver
package service

import zio.*
import entity.Article

trait ArticleService:
  import ArticleService.*

  def get(id: Long): UIO[Either[DatabaseFailure, Option[Article]]]
  def create(
      name: String,
      content: String,
      authorName: String
  ): UIO[Either[DatabaseFailure, Article]]

object ArticleService:
  case class DatabaseFailure(cause: Throwable)
