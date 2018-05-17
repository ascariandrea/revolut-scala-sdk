package com.ascariandrea.revolut.models

import io.circe.generic.JsonCodec

@JsonCodec case class Account(
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
