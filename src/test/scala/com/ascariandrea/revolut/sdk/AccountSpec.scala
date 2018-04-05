package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.Uri
import com.ascariandrea.revolut.sdk.client.Client
import com.ascariandrea.revolut.sdk.models.Account
import com.ascariandrea.revolut.sdk.test.MockRevolutServer
import okhttp3.mockwebserver.MockWebServer
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import scala.concurrent.ExecutionContext.Implicits.global

class AccountSpec
    extends FlatSpec
    with Matchers
    with BeforeAndAfterAll
    with ScalaFutures {

  val server: MockWebServer = MockRevolutServer()
  var accountClient: Accounts = _

  override protected def beforeAll(): Unit = {
    server.start()
    accountClient = new Accounts(new Client(Uri(server.url("/api").toString)))
  }

  override protected def afterAll(): Unit = {
    server.shutdown()
  }

  it should "get all account" in {
    accountClient.getAll().map { result =>
      {
        result should be('right)
        result shouldBe List
      }
    }

  }

  it should "get an account" in {
    accountClient.get("42").map { result =>
      {
        result should be('right)
        result shouldBe Some(
          Account(
            "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
            "Current GBP account",
            100.0,
            "GBP",
            "active",
            public = false,
            "2017-06-01T11:11:11.0Z",
            "2017-06-01T11:11:11.0Z",
            "pocket"
          ))
      }
    }

  }
}
