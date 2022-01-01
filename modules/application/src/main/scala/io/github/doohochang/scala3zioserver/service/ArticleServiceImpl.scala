package io.github.doohochang.scala3zioserver
package service

import zio.*
import entity.Article
import repository.ArticleRepository

class ArticleServiceImpl(repository: ArticleRepository) extends ArticleService:
  import ArticleService.*

  def get(id: Long): UIO[Either[DatabaseFailure, Option[Article]]] =
    repository
      .get(id)
      .fold(cause => Left(DatabaseFailure(cause)), Right(_))

  def create(
      name: String,
      content: String,
      authorName: String
  ): UIO[Either[DatabaseFailure, Article]] =
    repository
      .create(name, content, authorName)
      .fold(cause => Left(DatabaseFailure(cause)), Right(_))

object ArticleServiceImpl:
  val layer: URLayer[Has[ArticleRepository], Has[ArticleService]] =
    ZLayer.fromEffect(
      for repository <- ZIO.service[ArticleRepository]
      yield ArticleServiceImpl(repository)
    )
