package com.ascariandrea.revolut.sdk.test

import com.ascariandrea.revolut.sdk.models.{Account, Counterparty}
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

  private val account = random[Account].asJson.toString()
  private val accounts = random[Account](10).toList.asJson.toString()

  private val counterparty =
    random[Counterparty].asJson.toString()

  private val counterparties = random[Counterparty](10).toList.asJson.toString()

  def create(): MockWebServer = {
    val server: MockWebServer = new MockWebServer()
    server.setDispatcher(new Dispatcher {
      override def dispatch(request: RecordedRequest): MockResponse = {
        request.getPath match {
          case "/api/accounts" =>
            new MockResponse().setResponseCode(200).setBody(accounts)
          case "/api/accounts/42" =>
            new MockResponse().setResponseCode(200).setBody(account)
          case "/api/counterparties" =>
            new MockResponse().setResponseCode(200).setBody(counterparties)
          case "/api/counterparty/42" =>
            new MockResponse().setResponseCode(200).setBody(counterparty)
          case _ => new MockResponse().setResponseCode(404).setBody("not found")
        }
      }
    })
    server
  }
}
