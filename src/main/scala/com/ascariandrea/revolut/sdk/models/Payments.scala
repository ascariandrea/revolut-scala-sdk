package com.ascariandrea.revolut.sdk.models

import akka.http.scaladsl.model.DateTime
import io.buildo.enumero.annotations.enum
import io.buildo.enumero.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._

@enum trait TransferState {
  pending
  completed
  declined
  failed
}
@enum trait Reason {
  declined
  failed
}

@JsonCodec case class TransferData(
    request_id: String,
    source_account_id: String,
    target_account_id: String,
    amount: Int,
    currency: String,
    description: String
)

@JsonCodec case class Transfer(
    id: String,
    state: TransferState,
    created_at: DateTime
)

@JsonCodec case class PaymentReceiver(
    counterparty_id: String,
    account_id: Option[String]
)

@JsonCodec case class PaymentData(
    request_id: String,
    account_id: String,
    receiver: PaymentReceiver,
    amount: Int,
    currency: String,
    description: String,
    schedule_for: Option[DateTime]
)

@JsonCodec case class Payment(
    id: String,
    state: TransferState,
    reason: String,
    created_at: DateTime
)

@JsonCodec case class Card(
    card_number: String,
    first_name: String,
    last_name: String,
    phone: String
)

@JsonCodec case class LegCounterparty(
    id: String,
    account_id: String,
    `type`: CounterpartyType
)

@JsonCodec case class Leg(
    leg_id: String,
    amount: Int,
    currency: String,
    bill_amount: Option[Int],
    bill_currency: Option[String],
    account_id: String,
    counterparty: LegCounterparty,
    description: String,
    explanation: String,
    card: Card
)

@JsonCodec case class Merchant(
    name: String,
    city: String,
    category_code: String,
    country: String
)

@JsonCodec case class Transaction(
    id: String,
    `type`: String,
    request_id: String,
    state: TransferState,
    reason: Option[Reason],
    created_at: DateTime,
    updated_at: DateTime,
    completed_at: Option[DateTime],
    schedule_for: Option[DateTime],
    merchant: Option[Merchant],
    legs: List[Leg]
)

@JsonCodec case class TransactionsParams(
    from: Option[DateTime],
    to: Option[DateTime],
    counterparty: Option[String],
    count: Option[Int],
    `type`: Option[String]
)
