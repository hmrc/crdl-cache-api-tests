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

package uk.gov.hmrc.api.client

import play.api.libs.ws.DefaultBodyWritables.*
import play.api.libs.ws.{EmptyBody, StandaloneWSResponse}
import uk.gov.hmrc.api.specs.BaseSpec
import uk.gov.hmrc.apitestrunner.http.HttpClient as TestRunnerHttpClient

import java.util.UUID
import scala.concurrent.Future

trait HttpClient extends TestRunnerHttpClient:
  this: BaseSpec =>

  val InternalAuthToken = UUID.randomUUID().toString

  def get(url: String, headers: (String, String)*): Future[StandaloneWSResponse] =
    mkRequest(url)
      .withHttpHeaders(headers*)
      .get()

  def post(url: String, headers: (String, String)*): Future[StandaloneWSResponse] =
    mkRequest(url)
      .withHttpHeaders(headers*)
      .post(EmptyBody)

  def post(url: String, bodyAsJson: String, headers: (String, String)*): Future[StandaloneWSResponse] =
    mkRequest(url)
      .withHttpHeaders(headers*)
      .post(bodyAsJson)

  def delete(url: String, headers: (String, String)*): Future[StandaloneWSResponse] =
    mkRequest(url)
      .withHttpHeaders(headers*)
      .delete()

  def createInternalAuthToken(): StandaloneWSResponse =
    await(
      post(
        "http://localhost:8470/test-only/token",
        s"""{
           |  "token": "$InternalAuthToken",
           |  "principal": "emcs-tfe-crdl-reference-data",
           |  "permissions": [{
           |    "resourceType": "crdl-cache",
           |    "resourceLocation": "*",
           |    "actions": ["READ"]
           |  }]
           |}""".stripMargin,
        "Content-Type" -> "application/json"
      )
    )

  def deleteList(list: String): StandaloneWSResponse = await(delete(s"$testOnlyHost/$list"))

  def deleteLastUpdated(): StandaloneWSResponse = await(delete(s"$testOnlyHost/last-updated"))

  def importLists(list: String): StandaloneWSResponse = await(post(s"$testOnlyHost/$list"))

  def getImportStatus(list: String): StandaloneWSResponse = await(get(s"$testOnlyHost/$list"))

  def getCodelist(code: String): StandaloneWSResponse = await(
    get(s"$host/lists/$code", "Authorization" -> InternalAuthToken)
  )

  def getCodelistVersions(): StandaloneWSResponse = await(get(s"$host/lists", "Authorization" -> InternalAuthToken))

  def getCustomsOfficeList(): StandaloneWSResponse = await(get(s"$host/offices", "Authorization" -> InternalAuthToken))
