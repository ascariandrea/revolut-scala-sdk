package com.ascariandrea.revolut.sdk
package client

import java.io.IOException
import java.util.concurrent.CompletableFuture

import okhttp3.HttpUrl.Builder
import okhttp3.{HttpUrl, OkHttpClient, Request, Response}
import spray.json._

class Client(httpUrl: HttpUrl) extends OkHttpClient {

  val client: OkHttpClient = new OkHttpClient()

  def get[T: JsonReader](
      httpUrl: String): CompletableFuture[Either[IOException, Option[T]]] = {
    makeRequest[T](httpUrl)
      .thenApply((either: Either[IOException, Option[String]]) =>
        either.right.map(opt =>
          opt.map(jsValue => jsValue.parseJson.convertTo[T])))
  }

  def getMany[T: JsonReader](httpUrl: String)
    : CompletableFuture[Either[IOException, Option[List[T]]]] = {
    makeRequest[T](httpUrl)
      .thenApply((either: Either[IOException, Option[String]]) =>
        either.right.map[Option[List[T]]](opt =>
          opt.map(jsValue => {
            jsValue.parseJson match {
              case JsArray(elements) => elements.map(_.convertTo[T]).toList
              case _                 => Nil
            }
          })))
  }

  private def makeRequest[T: JsonReader](
      url: String): CompletableFuture[Either[IOException, Option[String]]] = {

    val path = httpUrl.newBuilder().addEncodedPathSegments(url).build()

    val request = new Request.Builder()
      .url(path)
      .build()

    val call = client.newCall(request)
    val result = new OkHttpClientResponseFuture()
    call.enqueue(result)

    result.future.thenApply[Either[IOException, Option[String]]](
      (either: Either[IOException, Option[Response]]) => {

        either.right
          .map(v => v.map(r => r.body().string()))

      })
  }

}
