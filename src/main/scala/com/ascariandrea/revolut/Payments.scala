package com.ascariandrea.revolut

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.Uri.{Path, Query}
import akka.http.scaladsl.model.Uri.Path.Slash
import com.ascariandrea.revolut.client.Client
import com.ascariandrea.revolut.models._
import io.circe.syntax._
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

  def transactions(params: TransactionsParams)
    : Future[Either[HttpResponse, Option[List[Transaction]]]] =
    client.getMany[Transaction](
      Slash(Path("transactions")),
      Query(
        Map[String, Option[String]](
          "`type`" -> params.`type`,
          "counterparty" -> params.counterparty,
          "from" -> params.from.map(d => d.toString),
          "to" -> params.to.map(d => d.toString)
        ).filter(e => e._2.isDefined)
          .mapValues(sOpt => sOpt.get)
      )
    )
}
