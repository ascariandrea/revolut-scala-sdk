package com.ascariandrea.revolut

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import com.typesafe.config.{Config, ConfigFactory}
import client.Client

class Revolut(val sandbox: Boolean, val apiKey: String) {

  private val resources: Config = ConfigFactory.load().getConfig("revolut")

  private val hostResourceKey: String = if (sandbox) "sandbox" else "live"
  private val host: String =
    resources.getString(hostResourceKey)

  private val baseUrl: Uri =
    Uri(host).withScheme("https").withPath(Path("/api"))

  private val client: Client = new Client(baseUrl, apiKey)

  val accounts = new Accounts(this.client)
  val counterparties = new Counterparties(this.client)
  val payments = new Payments(this.client)
}

object Revolut {
  def apply(apiKey: String): Revolut = new Revolut(false, apiKey)

  def apply(sandbox: Boolean, apiKey: String): Revolut = new Revolut(
    sandbox,
    apiKey
  )
}
