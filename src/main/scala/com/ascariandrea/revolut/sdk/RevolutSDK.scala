package com.ascariandrea.revolut.sdk

import models.Config
import client.Client
import okhttp3.HttpUrl

class RevolutSDK(config: Config) {

  val version: String = "1.0"
  val host: String =
    if (config.sandbox) "sandbox-b2b.revolut.com"
    else "b2b.revolut.com"

  private val apiUrl = new HttpUrl.Builder()
    .scheme("https")
    .host(host)
    .addPathSegment("api")
    .build()

  val client: Client = new Client(apiUrl)

  val accounts = new Accounts(this.client)
}
