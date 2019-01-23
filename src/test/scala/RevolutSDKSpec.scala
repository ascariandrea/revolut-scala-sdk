package com.ascariandrea.revolut
package test

import com.ascariandrea.revolut.Revolut
import org.scalatest._

class RevolutSDKSpec extends FlatSpec with Matchers {

  it should "instantiate the class" in {
    val revolut = new Revolut(true, "my-api-key")
    revolut should not be null
  }
}
