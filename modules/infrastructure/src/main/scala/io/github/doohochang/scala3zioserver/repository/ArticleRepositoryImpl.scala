package io.github.doohochang.scala3zioserver
package repository

import zio.*
import zio.clock.Clock
import zio.blocking.Blocking
import zio.interop.catz._
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Transactor

import entity.Article

class ArticleRepositoryImpl(transactor: Transactor[Task])(using
    Runtime[Clock with Blocking]
) extends ArticleRepository:

  def get(id: Long): Task[Option[Article]] =
    sql"""select id, name, content, author_name
        from articles 
        where id = $id"""
      .query[(Long, String, String, String)]
      .map(Article.apply.tupled)
      .to[List]
      .transact(transactor)
      .map(_.headOption)

  def create(name: String, content: String, authorName: String): Task[Article] =
    sql"""INSERT INTO articles (name, content, author_name)
         VALUES ($name, $content, $authorName)
         RETURNING id, name, content, author_name"""
      .query[(Long, String, String, String)]
      .map(Article.apply.tupled)
      .to[List]
      .transact(transactor)
      .map(_.head)

object ArticleRepositoryImpl:
  val layer: URLayer[Has[Transactor[Task]] with Clock with Blocking, Has[
    ArticleRepository
  ]] =
    ZLayer.fromEffect(
      for
        transactor <- ZIO.service[Transactor[Task]]
        runtime <- ZIO.runtime[Clock with Blocking]
      yield ArticleRepositoryImpl(transactor)(using runtime)
    )
