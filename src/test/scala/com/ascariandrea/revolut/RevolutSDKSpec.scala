package com.ascariandrea.revolut

import com.ascariandrea.revolut.models.Config
import org.scalatest._

class RevolutSDKSpec extends FlatSpec with Matchers {

  it should "instantiate the class" in {
    val revolutSDK = new RevolutSDK(Config(true, Some("my-api-key")))
    revolutSDK should not be null
  }
}
