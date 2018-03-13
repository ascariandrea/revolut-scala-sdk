package com.ascariandrea.revolut.sdk

import com.ascariandrea.revolut.sdk.client.Client
import com.ascariandrea.revolut.sdk.models.Account
import com.ascariandrea.revolut.sdk.test.MockRevolutServer
import okhttp3.mockwebserver.MockWebServer
import org.scalatest._

class AccountSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  val server: MockWebServer = MockRevolutServer()
  var accountClient: Accounts = null

  override protected def beforeAll(): Unit = {
    server.start()
    accountClient = new Accounts(new Client(server.url("/api")))
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  it should "get all account" in {
    val accountEither = accountClient.getAll().get()
    accountEither should be('right)
    accountEither.right.get.get should have length 3
  }

  it should "get an account" in {
    val accountEither = accountClient.get("42").get()
    accountEither should be('right)
    accountEither.right.get should be(
      Some(
        Account(
          "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
          "Current GBP account",
          100.0,
          "GBP",
          "active",
          false,
          "2017-06-01T11:11:11.0Z",
          "2017-06-01T11:11:11.0Z",
          "pocket"
        )))
  }
}
