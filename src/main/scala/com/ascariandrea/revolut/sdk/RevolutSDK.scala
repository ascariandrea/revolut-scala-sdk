package com.ascariandrea.revolut.sdk

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.Path
import models.Config
import client.Client

class RevolutSDK(config: Config) {

  val version: String = "1.0"
  val host: String =
    if (config.sandbox) "sandbox-b2b.revolut.com"
    else "b2b.revolut.com"

  val baseUrl: Uri = Uri(host).withScheme("https").withPath(Path("/api"))

  val client: Client = new Client(baseUrl)

  val accounts = new Accounts(this.client)
  val counterparties = new Counterparties(this.client)
}
