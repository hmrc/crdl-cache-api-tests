/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.api.specs
import org.scalatest.BeforeAndAfterAll
import play.api.libs.json.Reads.*
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.DefaultBodyReadables.*
import play.api.libs.ws.JsonBodyReadables.readableAsJson
import uk.gov.hmrc.api.client.HttpClient

import java.time.Instant

class ImportCodelistAPISpec extends BaseSpec, HttpClient, BeforeAndAfterAll:
  override def beforeAll(): Unit = {
    deleteList("codelists")
    deleteLastUpdated()
    deleteList("correspondence-lists")
    importLists("codelists").status            shouldBe 202
    importLists("correspondence-lists").status shouldBe 202
    eventually {
      // Wait for the import job to finish
      val codelistResponse = getImportStatus("codelists").body[JsValue]
      codelistResponse shouldBe Json.obj("status" -> "IDLE")
      val correspondenceListResponse = getImportStatus("correspondence-lists").body[JsValue]
      correspondenceListResponse shouldBe Json.obj("status" -> "IDLE")
    }
  }

  Feature("User can test the import CountryCodes codelist") {
    Scenario("To verify whether Get CountryCodes codelist request executes successfully") {
      Given("The endpoint is accessed")
      val getCodelist_response = getCodelist("BC08")
      getCodelist_response.status        shouldBe 200
      getCodelist_response.body[JsValue] shouldBe Json.parse("""[{
            |   "key": "BL",
            |  "value": "Saint Barthélemy",
            |  "properties": {
            |    "actionIdentification": "823"
            |  }
            | } ,
            | {
            |  "key": "BM",
            |  "value": "Bermuda",
            |  "properties": {
            |    "actionIdentification": "824"
            |  }
            | },
            | {
            |  "key": "CX",
            |  "value": "Christmas Island",
            |  "properties": {
            |    "actionIdentification": "848"
            |  }
            | },
            | {
            |  "key": "CY",
            |  "value": "Cyprus",
            |  "properties": {
            |    "actionIdentification": "849"
            |  }
            |  }
]""".stripMargin)
    }

    Scenario("To verify if Get Codelists request returns 400 error with invalid CodeList") {
      Given("The endpoint is accessed")
      val getCodelist_response = getCodelist("BC88")
      getCodelist_response.status shouldBe 400
      getCodelist_response.body
    }

    Scenario("Verify the lastUpdated dates and snapshot version of Codelist codes is as expected") {
      Given("The endpoint is accessed")
      val getCodelistVersions_response = getCodelistVersions()
      getCodelistVersions_response.status shouldBe 200
      val now               = Instant.now()
      val Json_response     = getCodelistVersions_response.body[JsValue].as[List[JsObject]]
      val lastUpdated_Dates = Json_response
        // E200 currently uses static data with a fixed import timestamp
        .filterNot(_("codeListCode").as[String] == "E200")
        .map(_ \ "lastUpdated")
        .map(_.as[Instant])
      every(lastUpdated_Dates) should (be >= now.minusSeconds(60))
      Json_response.foreach { obj =>
        if (obj("codeListCode").as[String] == "BC08")
          obj("snapshotVersion").as[Long] shouldBe 12
        else if (obj("codeListCode").as[String] == "BC66")
          obj("snapshotVersion").as[Long] shouldBe 9
        else if (obj("codeListCode").as[String] == "BC36")
          obj("snapshotVersion").as[Long] shouldBe 9
        else if (obj("codeListCode").as[String] == "BC22")
          obj("snapshotVersion").as[Long] shouldBe 6
        else if (obj("codeListCode").as[String] == "BC01")
          obj("snapshotVersion").as[Long] shouldBe 9
        else if (obj("codeListCode").as[String] == "BC12")
          obj("snapshotVersion").as[Long] shouldBe 33
      }
    }

    Scenario("Verify fetching multiple codelist requests by Keys") {
      Given("The endpoint is accessed")
      val testOnlyUrl                = s"$host/lists/BC66?keys=B,W&keys=S"
      val getCodelistByKeys_response = await(
        get(
          testOnlyUrl
        )
      )
      getCodelistByKeys_response.status        shouldBe 200
      getCodelistByKeys_response.body[JsValue] shouldBe Json.parse("""[{
            |   "key": "B",
            |  "value": "Beer",
            |  "properties": {
            |    "actionIdentification": "1084"
            |  }
            | } ,
            | {
            |  "key": "S",
            |  "value": "Ethyl alcohol and spirits",
            |  "properties": {
            |    "actionIdentification": "1087"
            |  }
            | },
            | {
            |  "key": "W",
            |  "value": "Wine and fermented beverages other than wine and beer",
            |  "properties": {
            |    "actionIdentification": "1089"
            |  }
            |}
            ]""".stripMargin)
    }

    Scenario("Verify fetching codelists by properties") {
      Given("The endpoint is accessed")
      val testOnlyUrl                =
        s"$host/lists/BC36?exciseProductsCategoryCode=E&densityApplicabilityFlag=false&alcoholicStrengthApplicabilityFlag=false"
      val getCodelistByKeys_response = await(
        get(
          testOnlyUrl
        )
      )
      getCodelistByKeys_response.status        shouldBe 200
      getCodelistByKeys_response.body[JsValue] shouldBe Json.parse("""[{
            |  "key": "E470",
            |  "value": "Heavy fuel oil falling within CN codes 2710 19 62, 2710 19 66, 2710 19 67, 2710 20 32 and 2710 20 38 (Article 20(1)(c) of Directive 2003/96/EC)",
            |  "properties": {
            |    "unitOfMeasureCode": "1",
            |    "degreePlatoApplicabilityFlag": false,
            |    "actionIdentification": "1099",
            |    "exciseProductsCategoryCode": "E",
            |    "alcoholicStrengthApplicabilityFlag": false,
            |    "densityApplicabilityFlag": false
            |  }
            | } ,
            | {
            |  "key": "E500",
            |  "value": "Liquified Petroleum gases (LPG) Products falling within CN codes 2711 (except 2711 11, 2711 21 and 2711 29)",
            |  "properties": {
            |    "unitOfMeasureCode": "1",
            |    "degreePlatoApplicabilityFlag": false,
            |    "actionIdentification": "1102",
            |    "exciseProductsCategoryCode": "E",
            |    "alcoholicStrengthApplicabilityFlag": false,
            |    "densityApplicabilityFlag": false
            |  }
            | },
            |{
            |  "key": "E600",
            |  "value": "Saturated acyclic hydrocarbons Products falling within CN code 2901 10",
            |  "properties": {
            |    "unitOfMeasureCode": "1",
            |    "degreePlatoApplicabilityFlag": false,
            |    "actionIdentification": "1103",
            |    "exciseProductsCategoryCode": "E",
            |    "alcoholicStrengthApplicabilityFlag": false,
            |    "densityApplicabilityFlag": false
            |    }
            | },
            | {
            |  "key": "E930",
            |   "value": "Additives falling within CN codes 3811 11, 3811 19 00 and 3811 90 00",
            |  "properties": {
            |   "unitOfMeasureCode": "2",
            |   "degreePlatoApplicabilityFlag": false,
            |   "actionIdentification": "1108",
            |   "exciseProductsCategoryCode": "E",
            |   "alcoholicStrengthApplicabilityFlag": false,
            |   "densityApplicabilityFlag": false
            |  }
            | }
            ]""".stripMargin)
    }

    Scenario("To verify Delete Codelist request is successful") {
      Given("The endpoint is accessed")
      val deleteCodelist_response = deleteList("codelists")
      deleteCodelist_response.status shouldBe 200
      val getCodelist_response = getCodelist("BC08")
      getCodelist_response.status        shouldBe 200
      getCodelist_response.body[JsValue] shouldBe Json.arr()
    }

    Scenario("To verify Delete lastUpdated is successful") {
      Given("The endpoint is accessed")
      val deleteLastUpdated_response = deleteLastUpdated()
      deleteLastUpdated_response.status shouldBe 200
      val getCodelistVersions_response = getCodelistVersions()
      getCodelistVersions_response.status        shouldBe 200
      getCodelistVersions_response.body[JsValue] shouldBe Json.arr()
    }
  }

  Scenario("To verify whether Get Correspondence list executes successfully") {
    Given("The endpoint is accessed")
    val getCodelist_response = getCodelist("E200")
    getCodelist_response.status                                  shouldBe 200
    getCodelist_response.body[JsValue].as[List[JsValue]].take(4) shouldBe List(
      Json.parse("""{
          |   "key": "15071010",
          |  "value": "E200",
          |  "properties": {
          |    "actionIdentification": "408"
          |  }
          | }
          |""".stripMargin),
      Json.parse(""" {
          |  "key": "15079010",
          |  "value": "E200",
          |  "properties": {
          |    "actionIdentification": "409"
          |  }
          | }
          |""".stripMargin),
      Json.parse(""" {
          |  "key": "15081010",
          |  "value": "E200",
          |  "properties": {
          |    "actionIdentification": "410"
          |  }
          | }
          |""".stripMargin),
      Json.parse("""{
          |  "key": "15089010",
          |  "value": "E200",
          |  "properties": {
          |    "actionIdentification": "411"
          |  }
          |  }
          |""".stripMargin)
    )
  }
