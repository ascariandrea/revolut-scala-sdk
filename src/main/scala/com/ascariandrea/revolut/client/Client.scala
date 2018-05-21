package com.ascariandrea.revolut.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpHeader.ParsingResult.{Ok, Error}
import akka.http.scaladsl.model.Uri.{Path, Query}
import akka.http.scaladsl.model._
import akka.http.scaladsl.{Http, model}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import io.circe.{Decoder, Json}

import scala.concurrent.{ExecutionContextExecutor, Future}
import io.circe.parser._

class Client(val baseUrl: Uri, val apiKey: String) {

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val authorizationHeader: Option[HttpHeader] =
    HttpHeader.parse("Authorization", s"Bearer $apiKey") match {
      case Ok(header, _) => Some(header)
      case Error(_)      => None
    }

  def get[T: Decoder](path: Path): Future[Either[HttpResponse, Option[T]]] =
    get(path, Query.Empty)

  def get[T: Decoder](path: Path,
                      query: Query): Future[Either[HttpResponse, Option[T]]] =
    makeRequest(path, model.HttpMethods.GET, query, None)
      .map(serialize[T])

  def getMany[T: Decoder](
      path: Path): Future[Either[HttpResponse, Option[List[T]]]] =
    getMany(path, Query.Empty)

  def getMany[T: Decoder](
      path: Path,
      query: Query): Future[Either[HttpResponse, Option[List[T]]]] =
    makeRequest(path, model.HttpMethods.GET, query, None)
      .map(serialize[List[T]])

  def post[T: Decoder](
      path: Path,
      data: Option[Json]): Future[Either[HttpResponse, Option[T]]] =
    makeRequest(path, model.HttpMethods.POST, Query.Empty, data)
      .map(serialize[T])

  def delete(
      path: Path
  ): Future[Either[HttpResponse, Boolean]] =
    makeRequest(path, model.HttpMethods.DELETE, Query.Empty, None)
      .map(r => r.map(_ => true))

  private def makeRequest(
      path: Path,
      method: model.HttpMethod,
      query: Query,
      data: Option[Json]): Future[Either[HttpResponse, Option[String]]] = {

    val requestUri = Uri(
      baseUrl
        .withPath(baseUrl.path ++ path)
        .withQuery(query)
        .toString())

    // build headers for request

    Http()
      .singleRequest(
        model.HttpRequest(
          method,
          requestUri,
          authorizationHeader.fold(List.empty[HttpHeader])(h => List(h)),
          data
            .map(d => HttpEntity(ContentTypes.`application/json`, d.toString()))
            .getOrElse(HttpEntity.empty(ContentTypes.`application/json`))
        )
      )
      .flatMap {
        case HttpResponse(StatusCodes.OK | StatusCodes.Created |
                          StatusCodes.NoContent,
                          _,
                          entity,
                          _) =>
          Unmarshal(entity).to[String].map(s => Right(Some(s)))

        case resp @ HttpResponse(_, _, _, _) => Future.apply(Left(resp))
      }
  }

  private def serialize[T: Decoder](
      either: Either[HttpResponse, Option[String]])
    : Either[HttpResponse, Option[T]] =
    either.right.map(json => json.map(decode[T]).flatMap(_.toOption))

}
