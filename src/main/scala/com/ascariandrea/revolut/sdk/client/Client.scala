package com.ascariandrea.revolut.sdk
package client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.model.{HttpResponse, StatusCodes, Uri}
import akka.http.scaladsl.{Http, model}
import akka.stream.ActorMaterializer
import io.circe.Decoder

import scala.concurrent.Future
import io.circe.parser._

class Client(baseUrl: Uri) {

  implicit val system = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  def get[T: Decoder](path: Path): Future[Either[HttpResponse, Option[T]]] = {
    makeRequest(path)
      .map((either: Either[HttpResponse, Option[String]]) =>
        either.right.map(opt => opt.map(decode[T]).flatMap(_.toOption)))
  }

  def getMany[T: Decoder](
      path: Path): Future[Either[HttpResponse, Option[List[T]]]] = {
    makeRequest(path)
      .map((either: Either[HttpResponse, Option[String]]) =>
        either.right.map[Option[List[T]]](opt =>
          opt.map(jsValue => {
            parse(jsValue) match {
              case Right(json) =>
                json.arrayOrObject(
                  Nil,
                  el =>
                    el.flatMap(e => decode[T](e.toString()).toOption).toList,
                  _ => Nil)
              case _ => Nil
            }
          })))
  }

  private def makeRequest(
      path: Path): Future[Either[HttpResponse, Option[String]]] = {

    val responseFuture =
      Http().singleRequest(model.HttpRequest(uri = baseUrl.withPath(path)))
    responseFuture.map {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        Right(Some(entity.toString()))
      case resp @ HttpResponse(_, _, _, _) => Left(resp)
    }
  }

}
