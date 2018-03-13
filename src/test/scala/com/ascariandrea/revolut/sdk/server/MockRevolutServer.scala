package com.ascariandrea.revolut.sdk.test

import okhttp3.mockwebserver.{
  Dispatcher,
  MockResponse,
  MockWebServer,
  RecordedRequest
}

object MockRevolutServer {

  val account = """{
    "id": "2a0d4d03-e26c-4159-9de1-c6bf3adfd8a1",
    "name": "Current GBP account",
    "balance": 100.0,
    "currency": "GBP",
    "state": "active",
    "public": false,
    "updated_at": "2017-06-01T11:11:11.0Z",
    "created_at": "2017-06-01T11:11:11.0Z",
    "type": "pocket"
  }"""

  val accounts = s"""[$account,
  {
    "id": "df8d6b20-0725-482e-a29e-fb09631480cf",
    "name": "EUR expenses account",
    "balance": 1234.0,
    "currency": "EUR",
    "state": "active",
    "public": false,
    "created_at": "2017-06-01T11:11:11.0Z",
    "updated_at": "2017-06-01T11:11:11.0Z",
    "type": "pocket"
  },
  {
    "id": "9e815f39-453c-44f9-86dd-433492db5735",
    "name": "John Doe",
    "balance": 20,
    "currency": "GBP",
    "state": "active",
    "public": false,
    "created_at": "2017-06-01T21:11:11.0Z",
    "updated_at": "2017-06-01T21:11:11.0Z",
    "type": "beneficiary"
  }]"""

  val server: MockWebServer = new MockWebServer()

  def apply(): MockWebServer = {
    server.setDispatcher(new Dispatcher {
      override def dispatch(request: RecordedRequest): MockResponse = {
        request.getPath() match {
          case "/api/accounts" =>
            new MockResponse().setResponseCode(200).setBody(accounts)
          case "/api/accounts/42" =>
            new MockResponse().setResponseCode(200).setBody(account)
          case _ => new MockResponse().setResponseCode(404).setBody("not found")
        }
      }
    })
    server
  }

}
