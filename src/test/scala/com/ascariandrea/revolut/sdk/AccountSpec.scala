package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.Uri
import com.ascariandrea.revolut.sdk.client.Client
import com.ascariandrea.revolut.sdk.models.Account
import com.ascariandrea.revolut.sdk.server.MockRevolutServer
import okhttp3.mockwebserver.MockWebServer
import org.scalatest.{Assertions, AsyncFunSuite, BeforeAndAfterAll, Matchers}

class AccountSpec
    extends AsyncFunSuite
    with Matchers
    with BeforeAndAfterAll
    with Assertions {

  val server: MockWebServer = MockRevolutServer.create()
  var accountClient: Accounts = _

  override protected def beforeAll(): Unit = {
    server.start()
    accountClient = new Accounts(
      new Client(Uri(server.url("/api").toString), "api-key"))
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  test("Get all accounts") {
    accountClient.getAll map { result =>
      assert(server.takeRequest().getPath == "/api/accounts")
      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Some[_]]
      result.right.get.get shouldBe a[List[_]]
      result.right.get.get.head shouldBe a[Account]
    }
  }

  test("Get an account") {
    accountClient.get("42") map { result =>
      assert(server.takeRequest().getPath == "/api/accounts/42")
      result shouldBe a[Right[_, _]]
      result.right.get shouldBe a[Some[_]]
      result.right.get.get shouldBe a[Account]
    }

  }
}
