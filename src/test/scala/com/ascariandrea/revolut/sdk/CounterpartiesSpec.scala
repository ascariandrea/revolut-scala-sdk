package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import com.ascariandrea.revolut.sdk.client.Client
import com.ascariandrea.revolut.sdk.models.{Account, Counterparty}
import com.ascariandrea.revolut.sdk.test.MockRevolutServer
import okhttp3.mockwebserver.MockWebServer
import org.scalatest.{AsyncFunSuite, BeforeAndAfterAll, Matchers}

class CounterpartiesSpec
    extends AsyncFunSuite
    with Matchers
    with BeforeAndAfterAll {

  val server: MockWebServer = MockRevolutServer.create()
  var client: Counterparties = _

  override protected def beforeAll(): Unit = {
    server.start()
    client = new Counterparties(
      new Client(Uri(server.url("/api").toString))
    )
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  test("get all counterparties") {
    client.getAll() map { result =>
      assert(server.takeRequest().getPath == "/api/counterparties")
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
}