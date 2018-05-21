package com.ascariandrea.revolut

import org.scalatest._

class RevolutSDKSpec extends FlatSpec with Matchers {

  it should "instantiate the class" in {
    val revolutSDK = new RevolutSDK(true, "my-api-key")
    revolutSDK should not be null
  }
}
