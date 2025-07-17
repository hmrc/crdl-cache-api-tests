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
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.JsonBodyReadables.readableAsJson
import uk.gov.hmrc.api.client.HttpClient

class ImportCustomsOfficelistAPISpec extends BaseSpec, HttpClient, BeforeAndAfterAll:
  override def beforeAll(): Unit = {
    deleteList("customs-office-lists")
    importLists("customs-office-lists").status shouldBe 202
    eventually {
      // Wait for the import job to finish
      val customsOfficeListImportResponse = getImportStatus("customs-office-lists").body[JsValue]
      customsOfficeListImportResponse shouldBe Json.obj("status" -> "IDLE")
    }
  }

  Feature("User can test the import for CustomsOffice list") {
    Scenario("To verify whether Get CustomsOffice list executes successfully") {
      Given("The endpoint is accessed")
      val getCustomOfficelist_response = getCustomsOfficeList()
      getCustomOfficelist_response.status        shouldBe 200
      getCustomOfficelist_response.body[JsValue] shouldBe Json.parse("""[{
          |    "referenceNumber": "IT314102",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": "ITP00023",
          |    "referenceNumberCompetentAuthorityOfEnquiry": "IT314102",
          |    "referenceNumberCompetentAuthorityOfRecovery": "IT314102",
          |    "referenceNumberTakeover": null,
          |    "countryCode": "IT",
          |    "emailAddress": "testo@it",
          |    "unLocodeId": null,
          |    "nctsEntryDate": null,
          |    "nearestOffice": null,
          |    "postalCode": "10043",
          |    "phoneNumber": "345 34234",
          |    "faxNumber": null,
          |    "telexNumber": null,
          |    "geoInfoCode": null,
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": "IT",
          |    "dedicatedTraderName": "TIN",
          |    "customsOfficeSpecificNotesCodes": [],
          |    "customsOfficeLsd": {
          |      "city": "ORBASSANO (TO)",
          |      "prefixSuffixLevel": "A",
          |      "languageCode": "IT",
          |      "spaceToAdd": true,
          |      "customsOfficeUsualName": "ORBASSANO",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "Prima Strada, 5"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonStartDate": "2024-01-01",
          |      "seasonName": "ALL YEAR",
          |      "seasonCode": 1,
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 5,
          |          "openingHoursTimeFirstPeriodFrom": "08:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "18:00:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "DEP",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "INC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "TRA",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EIN",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "ENT",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "DES",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "GUA",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REG",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ],
          |      "seasonEndDate": "9999-12-31"
          |    }
          |  },
          |  {
          |    "referenceNumber": "DK003102",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": null,
          |    "referenceNumberCompetentAuthorityOfEnquiry": "DK003102",
          |    "referenceNumberCompetentAuthorityOfRecovery": "DK003102",
          |    "referenceNumberTakeover": null,
          |    "countryCode": "DK",
          |    "emailAddress": "test@dk",
          |    "unLocodeId": null,
          |    "nctsEntryDate": null,
          |    "nearestOffice": null,
          |    "postalCode": "9850",
          |    "phoneNumber": "+45 342234 34543",
          |    "faxNumber": null,
          |    "telexNumber": null,
          |    "geoInfoCode": null,
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": null,
          |    "dedicatedTraderName": null,
          |    "customsOfficeSpecificNotesCodes": [
          |      "SN0009"
          |    ],
          |    "customsOfficeLsd": {
          |      "city": "Hirtshals",
          |      "languageCode": "DA",
          |      "spaceToAdd": false,
          |      "customsOfficeUsualName": "Hirtshals Toldekspedition",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "Dalsagervej 7"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonCode": 1,
          |      "seasonStartDate": "2018-01-01",
          |      "seasonEndDate": "2099-12-31",
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 5,
          |          "openingHoursTimeFirstPeriodFrom": "08:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "16:00:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "EXL",
          |              "trafficType": "P"
          |            },
          |            {
          |              "roleName": "EXL",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "P"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "P"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "PLA",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "RFC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "DIS",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "P"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "P"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ]
          |    }
          |  },
          |  {
          |    "referenceNumber": "IT223101",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": "ITP00002",
          |    "referenceNumberCompetentAuthorityOfEnquiry": "IT223101",
          |    "referenceNumberCompetentAuthorityOfRecovery": "IT223101",
          |    "referenceNumberTakeover": null,
          |    "countryCode": "IT",
          |    "emailAddress": "test@it",
          |    "unLocodeId": null,
          |    "nctsEntryDate": "2025-05-01",
          |    "nearestOffice": null,
          |    "postalCode": "40131",
          |    "phoneNumber": "1234 045483382",
          |    "faxNumber": "2343 34543",
          |    "telexNumber": null,
          |    "geoInfoCode": "Q",
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": "IT",
          |    "dedicatedTraderName": "TIN",
          |    "customsOfficeSpecificNotesCodes": [],
          |    "customsOfficeLsd": {
          |      "city": "BOLOGNA",
          |      "prefixSuffixLevel": "A",
          |      "languageCode": "IT",
          |      "spaceToAdd": true,
          |      "customsOfficeUsualName": "AEROPORTO DI BOLOGNA",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "VIA DELL'AEROPORTO, 1"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonStartDate": "2018-01-01",
          |      "seasonName": "ALL YEAR",
          |      "seasonCode": 1,
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 6,
          |          "openingHoursTimeFirstPeriodFrom": "00:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "23:59:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "DEP",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "INC",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "TXT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "DES",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "ENT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EXC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "REG",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "TRA",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EIN",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "PLA",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "DIS",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "RFC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ],
          |      "seasonEndDate": "2099-12-31"
          |    }
          |  },
          |  {
          |    "referenceNumber": "IT223100",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": "ITP00002",
          |    "referenceNumberCompetentAuthorityOfEnquiry": null,
          |    "referenceNumberCompetentAuthorityOfRecovery": null,
          |    "referenceNumberTakeover": null,
          |    "countryCode": "IT",
          |    "emailAddress": "test@test.it",
          |    "unLocodeId": null,
          |    "nctsEntryDate": "2025-05-01",
          |    "nearestOffice": null,
          |    "postalCode": "40121",
          |    "phoneNumber": "0039 435345",
          |    "faxNumber": "0039 435345",
          |    "telexNumber": null,
          |    "geoInfoCode": "Q",
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": "IT",
          |    "dedicatedTraderName": "TIN",
          |    "customsOfficeSpecificNotesCodes": [],
          |    "customsOfficeLsd": {
          |      "city": "BOLOGNA",
          |      "prefixSuffixLevel": "A",
          |      "languageCode": "IT",
          |      "spaceToAdd": true,
          |      "customsOfficeUsualName": "EMILIA 1 BOLOGNA",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "VIALE PIETRAMELLARA, 1/2"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonStartDate": "2018-01-01",
          |      "seasonName": "ALL YEAR",
          |      "seasonCode": 1,
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 6,
          |          "openingHoursTimeFirstPeriodFrom": "08:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "18:00:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "EXC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REG",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "SCO",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "PLA",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "DIS",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "RFC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ],
          |      "seasonEndDate": "2099-12-31"
          |    }
          |  }
           ]""".stripMargin)
    }

    Scenario("To verify whether user is able to filter CustomsOffice list by countryCode and roles") {
      Given("The endpoint is accessed")
      val testOnlyUrl                          = s"$host/offices?countryCodes=IT&roles=TRA"
      val getCustomsofficelistByRoles_response = await(
        get(
          testOnlyUrl
        )
      )
      getCustomsofficelistByRoles_response.status        shouldBe 200
      getCustomsofficelistByRoles_response.body[JsValue] shouldBe Json.parse("""[{
          |    "referenceNumber": "IT223101",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": "ITP00002",
          |    "referenceNumberCompetentAuthorityOfEnquiry": "IT223101",
          |    "referenceNumberCompetentAuthorityOfRecovery": "IT223101",
          |    "referenceNumberTakeover": null,
          |    "countryCode": "IT",
          |    "emailAddress": "test@it",
          |    "unLocodeId": null,
          |    "nctsEntryDate": "2025-05-01",
          |    "nearestOffice": null,
          |    "postalCode": "40131",
          |    "phoneNumber": "1234 045483382",
          |    "faxNumber": "2343 34543",
          |    "telexNumber": null,
          |    "geoInfoCode": "Q",
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": "IT",
          |    "dedicatedTraderName": "TIN",
          |    "customsOfficeSpecificNotesCodes": [],
          |    "customsOfficeLsd": {
          |      "city": "BOLOGNA",
          |      "prefixSuffixLevel": "A",
          |      "languageCode": "IT",
          |      "spaceToAdd": true,
          |      "customsOfficeUsualName": "AEROPORTO DI BOLOGNA",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "VIA DELL'AEROPORTO, 1"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonStartDate": "2018-01-01",
          |      "seasonName": "ALL YEAR",
          |      "seasonCode": 1,
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 6,
          |          "openingHoursTimeFirstPeriodFrom": "00:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "23:59:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "DEP",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "INC",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "TXT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "DES",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "ENT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EXC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "REG",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "TRA",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "EIN",
          |              "trafficType": "AIR"
          |            },
          |            {
          |              "roleName": "PLA",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "DIS",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "RFC",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ],
          |      "seasonEndDate": "2099-12-31"
          |    }
          |  },
          |  {
          |    "referenceNumber": "IT314102",
          |    "referenceNumberMainOffice": null,
          |    "referenceNumberHigherAuthority": "ITP00023",
          |    "referenceNumberCompetentAuthorityOfEnquiry": "IT314102",
          |    "referenceNumberCompetentAuthorityOfRecovery": "IT314102",
          |    "referenceNumberTakeover": null,
          |    "countryCode": "IT",
          |    "emailAddress": "testo@it",
          |    "unLocodeId": null,
          |    "nctsEntryDate": null,
          |    "nearestOffice": null,
          |    "postalCode": "10043",
          |    "phoneNumber": "345 34234",
          |    "faxNumber": null,
          |    "telexNumber": null,
          |    "geoInfoCode": null,
          |    "regionCode": null,
          |    "traderDedicated": false,
          |    "dedicatedTraderLanguageCode": "IT",
          |    "dedicatedTraderName": "TIN",
          |    "customsOfficeSpecificNotesCodes": [],
          |    "customsOfficeLsd": {
          |      "city": "ORBASSANO (TO)",
          |      "prefixSuffixLevel": "A",
          |      "languageCode": "IT",
          |      "spaceToAdd": true,
          |      "customsOfficeUsualName": "ORBASSANO",
          |      "prefixSuffixFlag": false,
          |      "streetAndNumber": "Prima Strada, 5"
          |    },
          |    "customsOfficeTimetable": {
          |      "seasonStartDate": "2024-01-01",
          |      "seasonName": "ALL YEAR",
          |      "seasonCode": 1,
          |      "customsOfficeTimetableLine": [
          |        {
          |          "dayInTheWeekEndDay": 5,
          |          "openingHoursTimeFirstPeriodFrom": "08:00:00",
          |          "dayInTheWeekBeginDay": 1,
          |          "openingHoursTimeFirstPeriodTo": "18:00:00",
          |          "customsOfficeRoleTrafficCompetence": [
          |            {
          |              "roleName": "DEP",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "INC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "TRA",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXP",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EIN",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "ENT",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "DES",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "GUA",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "EXT",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REG",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "REC",
          |              "trafficType": "R"
          |            },
          |            {
          |              "roleName": "IPR",
          |              "trafficType": "N/A"
          |            },
          |            {
          |              "roleName": "ENQ",
          |              "trafficType": "N/A"
          |            }
          |          ]
          |        }
          |      ],
          |      "seasonEndDate": "9999-12-31"
          |    }
          |  }
       ]""".stripMargin)
    }
  }
