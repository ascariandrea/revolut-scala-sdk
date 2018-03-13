package com.ascariandrea.revolut.sdk
package models

import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._

case class Account(
    id: String,
    name: String,
    balance: BigDecimal,
    currency: String,
    state: String,
    public: Boolean = false,
    created_at: String,
    updated_at: String,
    `type`: String
)

object Account {
  implicit val format: RootJsonFormat[Account] = jsonFormat9(Account.apply)
}
