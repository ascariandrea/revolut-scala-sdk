package com.ascariandrea.revolut

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.Path.Slash
import akka.http.scaladsl.model.Uri.{Path, Query}
import io.circe.syntax._
import client.Client
import models.{Counterparty, CounterpartyData}

import scala.concurrent.Future

class Counterparties(val client: Client) {

  def getAll(): Future[Either[HttpResponse, Option[List[Counterparty]]]] = {
    client.getMany[Counterparty](Slash(Path("counterparties")), Query.Empty)
  }

  def get(id: String): Future[Either[HttpResponse, Option[Counterparty]]] = {
    client.get[Counterparty](Slash(Path("counterparty")) / id)
  }

  def add(counterparty: CounterpartyData)
    : Future[Either[HttpResponse, Option[Counterparty]]] = {
    client.post[Counterparty](Slash(Path("counterparty")),
                              Some(counterparty.asJson))
  }

  def delete(id: String): Future[Either[HttpResponse, Boolean]] =
    client.delete(Slash(Path("counterparty")) / id)
}
