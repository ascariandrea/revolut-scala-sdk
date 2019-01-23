# Revolut SDK

[![CircleCI](https://circleci.com/gh/ascariandrea/revolut-scala-sdk.svg?style=svg)](https://circleci.com/gh/ascariandrea/revolut-scala-sdk)
[![Download](https://api.bintray.com/packages/ascariandrea/maven/revolut/images/download.svg?version=0.1.1-SNAPSHOT) ](https://bintray.com/ascariandrea/maven/revolut/0.1.1/link)

A SDK for revolut API written in Scala.

## Usage

Add bintray resolver to `build.sbt`

```scala
resolvers += "ascariandrea at bintray" at "https://dl.bintray.com/ascariandrea/maven"
```

```scala
import com.ascariandrea.revolut.Revolut
...

val revolut = new Revolut(sandbox = true, apiKey = "your-api-key")
```

#### Accounts

```scala
val accountF = revolut
  .accounts
  .get(accountId: String): Future[Either[HttpResponse, Option[Account]]];

val accountsF = revolut
  .accounts
  .getAll(): Future[Either[HttpResponse, Option[List[Account]]]];
```

#### Counterparties

```scala
val addCounterpartyF = revolut
  .counterparties
  .add(counterparty: Counterparty): Future[Either[HttpResponse, Option[Counterparty]]];

val countepartiesF = revolut
  .counterparties
  .getAll(): Future[Either[HttpResponse, Option[List[Counterparty]]]];

val counterpartyF = revolut
  .counterparties
  .get(counterpartyId: String): Future[Either[HttpResponse, Option[Counterparty]]];

val deleteCounterpartyF = revolut
  .counterparties
  .del(counterpartyId: String): Future[Either[HttpResponse, Option[Boolean]]];
```

#### Payments

```scala
val transferF = revolut
  .payments
  .transfer(transfer: TransferData): Future[Either[HttpResponse, Option[Traansfer]]];

val transactionsF = revolut
  .payments
  .pay(payment: PaymentData): Future[Either[HttpResponse, Option[Transaction]]];

val transactionF = revolut
  .payments
  .transactionById(transactionId: String): Future[Either[HttpResponse, Option[Transaction]]];

val transactionByRequestIdF = revolut
  .payments
  .transactionByRequestId(transactionRequestId: String): Future[Either[HttpResponse, Option[Transaction]]];

val cancelPaymentF = revolut
  .payments
  .cancel(paymentId: String): Future[Either[HttpResponse, Option[Boolean]]];

val transactionsF = revolut
  .payments
  .transactions(transactionsParams: Option[TransactionParams]): Future[Either[HttpResponse, Option[List[Transaction]]]];
```

## Test

```
$ sbt test
```
