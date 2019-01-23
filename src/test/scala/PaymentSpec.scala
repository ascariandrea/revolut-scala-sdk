package com.ascariandrea.revolut
package test

import akka.http.scaladsl.model.Uri
import com.ascariandrea.revolut.client.Client
import com.danielasfregola.randomdatagenerator.RandomDataGenerator._
import com.ascariandrea.revolut.models.{
  Payment,
  PaymentData,
  Transaction,
  TransactionsParams,
  Transfer,
  TransferData
}
import okhttp3.mockwebserver.MockWebServer
import org.scalatest.{Assertions, AsyncFunSuite, BeforeAndAfterAll, Matchers}
import server.MockRevolutServer

class PaymentSpec
    extends AsyncFunSuite
    with Matchers
    with BeforeAndAfterAll
    with Assertions {

  val server: MockWebServer = MockRevolutServer.create()
  var client: Payments = _

  override protected def beforeAll(): Unit = {
    server.start()
    client = new Payments(
      new Client(Uri(server.url("/api").toString), "api-key")
    )
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  test("create a transfer") {
    val transferData = random[TransferData]
    client.transfer(transferData).map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "POST")
      assert(req.getPath == "/api/transfer")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Option[_]]
      result.right.get.get shouldBe a[Transfer]
    }
  }

  test("create a payment") {
    val paymentData = random[PaymentData]
    client.pay(paymentData).map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "POST")
      assert(req.getPath == "/api/pay")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Option[_]]
      result.right.get.get shouldBe a[Payment]
    }
  }

  test("request a transaction by id") {
    client.transactionById("42").map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "GET")
      assert(req.getPath == "/api/transaction/42")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Option[_]]
      result.right.get.get shouldBe a[Transaction]
    }
  }

  test("request a transaction by request id") {
    client.transactionByRequestId("42").map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "GET")
      assert(req.getPath == "/api/transaction/42?id_type=request_id")
      assert(req.getRequestUrl.queryParameter("id_type") == "request_id")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Option[_]]
      result.right.get.get shouldBe a[Transaction]

    }
  }

  test("cancel a payment") {
    client.cancel("42").map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "DELETE")
      assert(req.getPath == "/api/transaction/42")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[java.lang.Boolean]
    }
  }

  test("get a list of transactions") {
    val transactionParams = random[TransactionsParams]
    client.transactions(Some(transactionParams)).map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "GET")
      assert(req.getPath.contains("/api/transactions"))

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Option[_]]
      result.right.get.get shouldBe a[List[_]]
      result.right.get.get.head shouldBe a[Transaction]
    }
  }
}
