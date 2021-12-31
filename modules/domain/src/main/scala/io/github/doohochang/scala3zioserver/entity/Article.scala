package io.github.doohochang.scala3zioserver.entity

case class Article(
    id: Long,
    name: String,
    content: String,
    authorName: String,
    viewCount: Long
)
