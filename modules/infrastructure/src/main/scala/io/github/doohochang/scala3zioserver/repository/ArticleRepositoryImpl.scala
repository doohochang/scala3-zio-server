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
    sql"""select id, name, content, author_name, view_count 
        from articles 
        where id = $id"""
      .query[(Long, String, String, String, Long)]
      .map(Article.apply.tupled)
      .to[List]
      .transact(transactor)
      .map(_.headOption)

  def create(name: String, content: String, authorName: String): Task[Article] =
    sql"""INSERT INTO articles (name, content, author_name)
         VALUES ($name, $content, $authorName)
         RETURNING id, name, content, author_name, view_count"""
      .query[(Long, String, String, String, Long)]
      .map(Article.apply.tupled)
      .to[List]
      .transact(transactor)
      .map(_.head)
