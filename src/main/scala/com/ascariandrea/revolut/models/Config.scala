package com.ascariandrea.revolut.models

case class Config(
    sandbox: Boolean = true,
    apiKey: Option[String] = None
)
