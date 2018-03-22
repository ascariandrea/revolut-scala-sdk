package com.ascariandrea.revolut.sdk
package models

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
import io.circe._
import io.circe.generic.semiauto._

object Account {
  // also works inlined
  implicit val decoderImplicit: Decoder[Account] = deriveDecoder
}
