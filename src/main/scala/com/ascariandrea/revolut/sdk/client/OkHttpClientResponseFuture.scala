package com.ascariandrea.revolut.sdk
package client

import java.io.IOException
import java.util.concurrent.CompletableFuture

import okhttp3.{Call, Callback, Response}

class OkHttpClientResponseFuture() extends Callback {
  val future: CompletableFuture[Either[IOException, Option[Response]]] =
    new CompletableFuture()

  override def onFailure(call: Call, e: IOException): Unit = {
    future.complete(Left(e))
    ()
  }

  override def onResponse(call: Call, response: Response): Unit = {
    future.complete(Right(Some(response)))
    ()
  }
}
