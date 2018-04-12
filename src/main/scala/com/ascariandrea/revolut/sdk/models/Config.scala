package com.ascariandrea.revolut.sdk
package models

case class Config(
    sandbox: Boolean = true,
    apiKey: Option[String] = None
)

