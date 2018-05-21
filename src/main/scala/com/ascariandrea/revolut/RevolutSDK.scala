package com.ascariandrea.revolut

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import client.Client
import com.typesafe.config.{Config, ConfigFactory}

class RevolutSDK(val sandbox: Boolean, val apiKey: String) {

  val resources: Config = ConfigFactory.load().getConfig("revolut")

  val version: String = "1.0"
  val host: String =
    resources.getString(if (sandbox) "sandbox" else "live")

  val baseUrl: Uri = Uri(host).withScheme("https").withPath(Path("/api"))

  val client: Client = new Client(baseUrl, apiKey)

  val accounts = new Accounts(this.client)
  val counterparties = new Counterparties(this.client)
  val payments = new Payments(this.client)
}
