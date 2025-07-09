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

class ImportCorrespondencelistAPISpec extends BaseSpec, HttpClient, BeforeAndAfterAll:
  override def beforeAll(): Unit = {
    deleteCodelist()
    deleteLastUpdated()
    deleteCorrespondenceList()
    importCorrespondenceList().status shouldBe 202
    eventually {
      // Wait for the import job to finish
      val correspondenceListResponse = getCorrespondencelistImportStatus().body[JsValue]
      correspondenceListResponse shouldBe Json.obj("status" -> "IDLE")
    }
  }

  Feature("User can test the import for Correspondence list") {
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
  }
