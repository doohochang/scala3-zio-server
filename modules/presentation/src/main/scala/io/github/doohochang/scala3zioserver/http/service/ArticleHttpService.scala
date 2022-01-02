package io.github.doohochang.scala3zioserver
package http.service

import cats.effect.*
import zio.*
import zio.interop.catz.*
import zio.json.*
import org.http4s.*
import org.http4s.dsl.Http4sDslBinCompat

import entity.Article
import entity.json.given
import service.ArticleService

class ArticleHttpService(service: ArticleService):
  import ArticleHttpService.*
  import dsl.*

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "articles" / LongVar(id) =>
      for
        articleResult <- service.get(id)
        response <- articleResult match
          case Left(failure) => InternalServerError(failure.cause.getMessage)
          case Right(articleOption) =>
            articleOption match
              case Some(article) => Ok.apply(article.toJson)
              case None          => NotFound()
      yield response

    case rawRequest @ POST -> Root / "articles" =>
      for
        requestString <- rawRequest.as[String]
        requestEither = requestString.fromJson[PostArticleRequest]
        response <- requestEither match
          case Left(parsingError) => BadRequest(parsingError)
          case Right(request) =>
            for
              resultEither <- service.create(
                request.name,
                request.content,
                request.authorName
              )
              response_ <- resultEither match
                case Left(failure) =>
                  InternalServerError(failure.cause.getMessage)
                case Right(article) => Ok(article.toJson)
            yield response_
      yield response
  }

object ArticleHttpService:
  val layer: URLayer[Has[ArticleService], Has[ArticleHttpService]] =
    ZLayer.fromEffect(
      for articleService <- ZIO.service[ArticleService]
      yield ArticleHttpService(articleService)
    )

  object dsl extends Http4sDslBinCompat[Task]

  case class PostArticleRequest(
      name: String,
      content: String,
      authorName: String
  )

  given JsonDecoder[PostArticleRequest] =
    DeriveJsonDecoder.gen[PostArticleRequest]
