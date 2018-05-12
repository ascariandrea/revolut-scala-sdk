package com.ascariandrea.revolut.sdk.server

import java.nio.charset.Charset
import com.ascariandrea.revolut.sdk.models.{Account, Counterparty}
import okhttp3.mockwebserver.{
  Dispatcher,
  MockResponse,
  MockWebServer,
  RecordedRequest
}
import com.danielasfregola.randomdatagenerator.RandomDataGenerator._
import io.circe.JsonObject
import io.circe.syntax._
import io.circe.parser._
import io.circe.generic.auto._

object MockRevolutServer {

  private val account = random[Account].asJson
  private val accounts = random[Account](10).toList.asJson

  private val counterparty = random[Counterparty].asJson
  private val counterparties = random[Counterparty](10).toList.asJson

  def create(): MockWebServer = {
    val server: MockWebServer = new MockWebServer()
    server.setDispatcher(new Dispatcher {
      override def dispatch(request: RecordedRequest): MockResponse = {
        val path = request.getPath

        request.getMethod match {
          case "GET" => {
            path match {
              case "/api/accounts" =>
                new MockResponse()
                  .setResponseCode(200)
                  .setBody(accounts.toString())
              case "/api/accounts/42" =>
                new MockResponse()
                  .setResponseCode(200)
                  .setBody(account.toString())
              case "/api/counterparty/42" =>
                new MockResponse()
                  .setResponseCode(200)
                  .setBody(counterparty.toString())
              case "/api/counterparties" =>
                new MockResponse()
                  .setResponseCode(200)
                  .setBody(counterparties.toString())
            }
          }
          case "POST" => {
            path match {
              case "/api/counterparty" => {

                val counterpartyData =
                  parse(request.getBody.readString(Charset.defaultCharset())).toOption
                    .getOrElse(
                      JsonObject.empty.asJson
                    )

                new MockResponse()
                  .setResponseCode(201)
                  .setBody(counterparty.deepMerge(counterpartyData).toString())
              }
            }

          }

          case "DELETE" => {
            path match {
              case "/api/counterparty/42" =>
                new MockResponse().setResponseCode(204)
            }
          }
          case _ => new MockResponse().setResponseCode(404).setBody("not found")
        }

      }
    })
    server
  }
}
