package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import models.Config
import client.Client
import com.typesafe.config.ConfigFactory

class RevolutSDK(config: Config) {

  val resources = ConfigFactory.load().getConfig("revolut")

  val version: String = "1.0"
  val host: String =
    resources.getString(if (config.sandbox) "sandbox" else "live")

  val baseUrl: Uri = Uri(host).withScheme("https").withPath(Path("/api"))

  val client: Client = new Client(baseUrl)

  val accounts = new Accounts(this.client)
  val counterparties = new Counterparties(this.client)
  val payments = new Payments(this.client)
}
