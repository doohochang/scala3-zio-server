package io.github.doohochang.scala3zioserver
package http

import cats.effect.*
import org.http4s.*
import zio.*
import zio.json.*
import zio.interop.catz.*

import service.ArticleService
import entity.json.given

class ArticleHttpRoutes(articleService: ArticleService):
  import ArticleHttpRoutes.*
  import http4sDsl.*

  given JsonDecoder[PostArticleRequest] =
    DeriveJsonDecoder.gen[PostArticleRequest]

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "articles" / LongVar(id) =>
      for
        articleResult <- articleService.get(id)
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
        response <- requestString.fromJson[PostArticleRequest] match
          case Left(parsingError) => BadRequest(parsingError)
          case Right(request) =>
            for
              resultEither <- articleService.create(
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

object ArticleHttpRoutes:
  val layer: URLayer[Has[ArticleService], Has[ArticleHttpRoutes]] =
    ZLayer.fromEffect(
      for articleService <- ZIO.service[ArticleService]
      yield ArticleHttpRoutes(articleService)
    )

  case class PostArticleRequest(
      name: String,
      content: String,
      authorName: String
  )
