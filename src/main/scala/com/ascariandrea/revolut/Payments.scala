package com.ascariandrea.revolut

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.Path.Slash
import akka.http.scaladsl.model.Uri.{Path, Query}
import io.circe.syntax._
import client.Client
import models._

import scala.concurrent.Future

class Payments(val client: Client) {

  def transfer(transferData: TransferData)
    : Future[Either[HttpResponse, Option[Transfer]]] =
    client.post[Transfer](Slash(Path("transfer")), Some(transferData.asJson))

  def pay(
      paymentData: PaymentData): Future[Either[HttpResponse, Option[Payment]]] =
    client.post[Payment](Slash(Path("pay")), Some(paymentData.asJson))

  def transactionById(
      id: String): Future[Either[HttpResponse, Option[Transaction]]] =
    client.get[Transaction](Slash(Path("transaction") / id))

  def transactionByRequestId(
      requestId: String): Future[Either[HttpResponse, Option[Transaction]]] =
    client.get[Transaction](Slash(Path("transaction") / requestId),
                            Query(("id_type", "request_id")))

  def cancel(transactionId: String): Future[Either[HttpResponse, Boolean]] =
    client.delete(Slash(Path("transaction") / transactionId))

  def transactions(params: Option[TransactionsParams])
    : Future[Either[HttpResponse, Option[List[Transaction]]]] = {

    val paramsMap = params
      .fold(Map.empty[String, String])(
        p =>
          Map[String, Option[String]](
            "`type`" -> p.`type`,
            "counterparty" -> p.counterparty,
            "from" -> p.from.map(d => d.toString),
            "to" -> p.to.map(d => d.toString)
          ).filter(e => e._2.isDefined)
            .mapValues(sOpt => sOpt.get))

    client.getMany[Transaction](
      Slash(Path("transactions")),
      Query(paramsMap)
    )
  }

}
