package io.github.doohochang.scala3zioserver
package repository

import zio.*

import entity.Article

trait ArticleRepository:
  def get(id: Long): Task[Option[Article]]
  def create(name: String, content: String, authorName: String): Task[Article]
