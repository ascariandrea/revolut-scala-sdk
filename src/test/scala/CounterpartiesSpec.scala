package com.ascariandrea.revolut
package test

import akka.http.scaladsl.model.Uri
import com.ascariandrea.revolut.client.Client
import com.danielasfregola.randomdatagenerator.RandomDataGenerator._
import com.ascariandrea.revolut.models.{Counterparty, CounterpartyData}
import okhttp3.mockwebserver.MockWebServer
import org.scalatest.{Assertions, AsyncFunSuite, BeforeAndAfterAll, Matchers}
import server.MockRevolutServer

class CounterpartiesSpec
    extends AsyncFunSuite
    with Matchers
    with BeforeAndAfterAll
    with Assertions {

  val server: MockWebServer = MockRevolutServer.create()
  var client: Counterparties = _

  override protected def beforeAll(): Unit = {
    server.start()
    client = new Counterparties(
      new Client(Uri(server.url("/api").toString), "api-key")
    )
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  test("add a couterparty") {
    val counterparty = random[CounterpartyData]

    client.add(counterparty) map { result =>
      val request = server.takeRequest()
      assert(request.getPath == "/api/counterparty")
      assert(request.getMethod == "POST")
      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Some[_]]
      result.right.get.get shouldBe a[Counterparty]
    }
  }

  test("get all counterparties") {
    client.getAll() map { result =>
      val req = server.takeRequest()

      assert(req.getMethod == "GET")
      assert(req.getPath == "/api/counterparties")

      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Some[_]]
      result.right.get.get shouldBe a[List[_]]
      result.right.get.get.head shouldBe a[Counterparty]
    }
  }

  test("get a counterparty") {
    client.get("42") map { result =>
      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Some[_]]
      result.right.get.get shouldBe a[Counterparty]
    }
  }

  test("delete a counteparty") {
    client.delete("42") map { result =>
      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[java.lang.Boolean]
      result.right.get shouldBe true
    }
  }
}
