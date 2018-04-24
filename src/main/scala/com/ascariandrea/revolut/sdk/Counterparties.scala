package com.ascariandrea.revolut.sdk

import com.ascariandrea.revolut.sdk.models.{Counterparty}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.Path
import com.ascariandrea.revolut.sdk.client.Client
import scala.concurrent.Future

class Counterparties(val client: Client) {

  def getAll: Future[Either[HttpResponse, Option[List[Counterparty]]]] = {
    client.getMany[Counterparty](Path("/counterparties"))
  }

  def get(id: String): Future[Either[HttpResponse, Option[Counterparty]]] = {
    client.get[Counterparty](Path("/counterparty") / id)
  }
}
