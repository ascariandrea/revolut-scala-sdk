package com.ascariandrea.revolut.sdk
package models

import io.buildo.enumero.annotations.enum
import io.buildo.enumero.circe._
import io.circe.generic.{JsonCodec}

@enum trait ProfileType {
  business
  personal
}

@enum trait State {
  created
  deleted
}

@enum trait Type {
  revolut
  external
}

@JsonCodec case class Counterparty(
    id: String,
    name: String,
    phone: String,
    profile_type: String,
    country: String,
    state: String,
    `type`: Type,
    created_at: String,
    updated_at: String,
    accounts: List[Account]
)

// import io.circe._
// import io.circe.generic.semiauto._

// object Counterparty {
//   // // also works inlined
//   implicit val counterpartyDecoder: Decoder[Counterparty] =
//     deriveDecoder[Counterparty]
//   implicit val counterpartyEncoder: Encoder[Counterparty] =
//     deriveEncoder[Counterparty]

//   implicit final def encodeCaseEnum[T <: CaseEnum: CaseEnumSerialization]
//     : Encoder[T] =
//     Encoder.instance(
//       caseEnum =>
//         Json.fromString(
//           implicitly[CaseEnumSerialization[T]].caseToString(caseEnum)))

//   implicit final def decodeCaseEnum[T <: CaseEnum: CaseEnumSerialization]
//     : Decoder[T] =
//     Decoder.instance { c =>
//       {
//         println(s"Cursor: ${c.as[String]}")
//         c.as[String] match {
//           case Right(s) =>
//             implicitly[CaseEnumSerialization[T]].caseFromString(s) match {
//               case Some(caseEnum) => Right(caseEnum)
//               case None           => Left(DecodingFailure("CaseEnum", c.history))
//             }
//           case l @ Left(_) => l.asInstanceOf[Decoder.Result[T]]
//         }
//       }

//     }
// }
