package com.ascariandrea.revolut.sdk.models

import io.buildo.enumero.annotations.enum
import io.circe.generic.JsonCodec
import io.buildo.enumero.circe._

@enum trait ProfileType {
  business
  personal
}

@enum trait State {
  created
  deleted
}

@enum trait CounterpartyType {
  revolut
  external
}

@JsonCodec case class Counterparty(
    id: String,
    name: String,
    email: String,
    phone: String,
    profile_type: ProfileType,
    country: String,
    state: String,
    `type`: CounterpartyType,
    created_at: String,
    updated_at: String,
    accounts: List[Account]
)

@JsonCodec case class CounterpartyData(
    name: String,
    profile_type: ProfileType,
    phone: String,
    email: String
)
