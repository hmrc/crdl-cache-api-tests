lazy val root = (project in file("."))
  .settings(
    name := "crdl-cache-api-tests",
    version := "0.1.0",
    scalaVersion := "3.3.4",
    libraryDependencies ++= Dependencies.test,
    Test / fork := true,
    Test / parallelExecution := false,
    (Compile / compile) := ((Compile / compile) dependsOn (Compile / scalafmtSbtCheck, Compile / scalafmtCheckAll)).value
  )
