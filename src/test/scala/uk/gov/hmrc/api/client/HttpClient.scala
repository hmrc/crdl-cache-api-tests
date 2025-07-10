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

import scala.concurrent.Future

trait HttpClient extends TestRunnerHttpClient:
  this: BaseSpec =>

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

  def deleteCodelist(): StandaloneWSResponse = await(delete(s"$testOnlyHost/codelists"))

  def deleteLastUpdated(): StandaloneWSResponse = await(delete(s"$testOnlyHost/last-updated"))

  def importCodelists(): StandaloneWSResponse = await(post(s"$testOnlyHost/codelists"))

  def getCodelistImportStatus(): StandaloneWSResponse = await(get(s"$testOnlyHost/codelists"))

  def getCodelist(code: String): StandaloneWSResponse = await(get(s"$host/lists/$code"))

  def getCodelistVersions(): StandaloneWSResponse = await(get(s"$host/lists"))

  def deleteCorrespondenceList(): StandaloneWSResponse = await(delete(s"$testOnlyHost/correspondence-lists"))

  def importCorrespondenceList(): StandaloneWSResponse = await(post(s"$testOnlyHost/correspondence-lists"))

  def getCorrespondencelistImportStatus(): StandaloneWSResponse = await(get(s"$testOnlyHost/correspondence-lists"))

  def deleteCustomsOfficeList(): StandaloneWSResponse = await(delete(s"$testOnlyHost/customs-office-lists"))

  def importCustomsOfficeList(): StandaloneWSResponse = await(post(s"$testOnlyHost/customs-office-lists"))

  def getCustomsOfficeListImportStatus(): StandaloneWSResponse = await(get(s"$testOnlyHost/customs-office-lists"))

  def getCustomsOfficeList(): StandaloneWSResponse = await(get(s"$host/offices"))
