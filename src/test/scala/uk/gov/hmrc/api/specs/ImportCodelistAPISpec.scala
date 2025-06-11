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

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.api.client.HttpClient
import play.api.libs.ws.JsonBodyReadables.readableAsJson

class ImportCodelistAPISpec extends BaseSpec, HttpClient:

  Feature("User can test the import CountryCodes codelist") {
    Scenario("To verify whether Post CountryCodes codelist request executes successfully") {
      Given("The endpoint is accessed")
      val url                     = s"$testOnlyHost/codelists"
      val importCodelist_response = await(
        post(
          url
        )
      )
      importCodelist_response.status shouldBe 202
      eventually {
        val testOnlyUrl          = s"$host/lists/BC08"
        val getCodelist_response = await(
          get(
            testOnlyUrl
          )
        )
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
    }

    Scenario("To verify if Get Codelists request returns 400 error with invalid CodeList") {
      Given("The endpoint is accessed")
      val url                     = s"$testOnlyHost/codelists"
      val importCodelist_response = await(
        post(
          url
        )
      )
      importCodelist_response.status shouldBe 202
      eventually {
        val testOnlyUrl          = s"$host/lists/BC88"
        val getCodelist_response = await(
          get(
            testOnlyUrl
          )
        )
        getCodelist_response.status shouldBe 400
        getCodelist_response.body
      }
    }

    Scenario("To verify Delete Codelist request is successful") {
      Given("The endpoint is accessed")
      val url                     = s"$testOnlyHost/codelists"
      val deleteCodelist_response = await(
        delete(
          url
        )
      )
      deleteCodelist_response.status shouldBe 200
      eventually {
        val testOnlyUrl          = s"$host/lists/BC08"
        val getCodelist_response = await(
          get(
            testOnlyUrl
          )
        )
        getCodelist_response.status        shouldBe 200
        getCodelist_response.body[JsValue] shouldBe Json.arr()
      }
    }
  }
