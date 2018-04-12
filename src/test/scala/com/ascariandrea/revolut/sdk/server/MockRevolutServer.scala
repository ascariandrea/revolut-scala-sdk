package com.ascariandrea.revolut.sdk.test

import com.ascariandrea.revolut.sdk.models.Account
import okhttp3.mockwebserver.{
  Dispatcher,
  MockResponse,
  MockWebServer,
  RecordedRequest
}
import com.danielasfregola.randomdatagenerator.RandomDataGenerator._
import io.circe.generic.auto._
import io.circe.syntax._

object MockRevolutServer {

  val account = random[Account].asJson.toString()
  val accounts = random[Account](10).toList.asJson.toString()

  val server: MockWebServer = new MockWebServer()

  def apply(): MockWebServer = {
    server.setDispatcher(new Dispatcher {
      override def dispatch(request: RecordedRequest): MockResponse = {
        request.getPath() match {
          case "/api/accounts" =>
            new MockResponse().setResponseCode(200).setBody(accounts)
          case "/api/accounts/42" =>
            new MockResponse().setResponseCode(200).setBody(account)
          case _ => new MockResponse().setResponseCode(404).setBody("not found")
        }
      }
    })
    server
  }

}
