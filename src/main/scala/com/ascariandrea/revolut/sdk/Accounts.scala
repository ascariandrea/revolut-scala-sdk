package com.ascariandrea.revolut.sdk

import java.io.IOException
import java.util.concurrent.CompletableFuture

import com.ascariandrea.revolut.sdk.client.Client

class Accounts(val client: Client) {

  def getAll()
    : CompletableFuture[Either[IOException, Option[List[models.Account]]]] = {
    client.getMany[models.Account]("accounts")
  }

  def get(id: String)
    : CompletableFuture[Either[IOException, Option[models.Account]]] = {
    client.get[models.Account](s"accounts/$id")
  }
}
