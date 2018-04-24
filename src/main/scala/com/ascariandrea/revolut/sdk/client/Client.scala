package com.ascariandrea.revolut.sdk
package client

import akka.actor.ActorSystem
import akka.http.javadsl.model.ContentType
import akka.http.javadsl.server.PathMatchers
import akka.http.scaladsl.model.ContentType.WithFixedCharset
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.{HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.{Http, model}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.ByteString
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import io.circe.parser._

class Client(val baseUrl: Uri) {

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def get[T: Decoder](path: Path): Future[Either[HttpResponse, Option[T]]] = {
    makeRequest(path)
      .map((either: Either[HttpResponse, Option[String]]) =>
        either.right.map(opt => opt.map(decode[T]).flatMap(_.toOption)))
  }

  def getMany[T: Decoder](
      path: Path): Future[Either[HttpResponse, Option[List[T]]]] = {
    makeRequest(path)
      .map(
        (either: Either[HttpResponse, Option[String]]) =>
          either.right
            .map(json => json.map(decode[List[T]]).flatMap(_.toOption)))
  }

  private def makeRequest(
      path: Path): Future[Either[HttpResponse, Option[String]]] = {

    val requestUri = Uri(
      baseUrl
        .withPath(baseUrl.path ++ path)
        .toString())

    val responseFuture =
      Http().singleRequest(model.HttpRequest(uri = requestUri))
    responseFuture.flatMap {
      case HttpResponse(StatusCodes.OK, _, entity, _) => {
        Unmarshal(entity).to[String].map(s => Right(Some(s)))
      }
      case resp @ HttpResponse(_, _, _, _) => Future.apply(Left(resp))
    }

  }

}
