package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.Path
import com.ascariandrea.revolut.sdk.client.Client

import scala.concurrent.Future

class Accounts(val client: Client) {

  def getAll(): Future[Either[HttpResponse, Option[List[models.Account]]]] = {
    client.getMany[models.Account](Path("/a ccounts"))
  }

  def get(id: String): Future[Either[HttpResponse, Option[models.Account]]] = {
    client.get[models.Account](Path("/accounts") / (id))
  }
}
