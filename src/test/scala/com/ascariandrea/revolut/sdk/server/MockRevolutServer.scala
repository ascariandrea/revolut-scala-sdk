package com.ascariandrea.revolut.sdk.server

import java.nio.charset.Charset

import com.ascariandrea.revolut.sdk.models._
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

  private val payment = random[Payment].asJson

  private val transfer = random[Transfer].asJson

  private val transaction = random[Transaction].asJson
  private val transactions = random[Transaction](10).toList.asJson

  def create(): MockWebServer = {
    val server: MockWebServer = new MockWebServer()
    server.setDispatcher(new Dispatcher {
      override def dispatch(request: RecordedRequest): MockResponse = {
        val requestUrl = request.getRequestUrl.encodedPath()

        request.getMethod match {
          case "GET" => {
            new MockResponse()
              .setResponseCode(200)
              .setBody((requestUrl match {
                case "/api/accounts"        => accounts
                case "/api/accounts/42"     => account
                case "/api/counterparty/42" => counterparty
                case "/api/counterparties"  => counterparties
                case "/api/transaction/42"  => transaction
                case "/api/transactions"    => transactions
              }).toString())

          }
          case "POST" => {
            val payload = parse(
              request.getBody.readString(Charset.defaultCharset())).toOption
              .getOrElse(
                JsonObject.empty.asJson
              )
            new MockResponse()
              .setResponseCode(201)
              .setBody((
                requestUrl match {
                  case "/api/counterparty" =>
                    counterparty.deepMerge(payload)

                  case "/api/transfer" =>
                    transfer.deepMerge(payload)

                  case "/api/pay" =>
                    payment.deepMerge(payload)
                }
              ).toString())

          }

          case "DELETE" =>
            new MockResponse().setResponseCode(requestUrl match {
              case "/api/counterparty/42" => 204
              case "/api/transaction/42"  => 204
              case _                      => 404
            })

          case _ => new MockResponse().setResponseCode(404).setBody("not found")
        }

      }
    })
    server
  }
}
