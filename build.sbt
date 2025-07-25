lazy val root = (project in file("."))
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    name := "crdl-cache-api-tests",
    version := "0.1.0",
    scalaVersion := "3.3.4",
    libraryDependencies ++= Dependencies.test,
    (Compile / compile) := ((Compile / compile) dependsOn (Compile / scalafmtSbtCheck, Compile / scalafmtCheckAll)).value
  )
