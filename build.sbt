name := "ShoppintCartFP"

version := "0.1"

import Dependencies._

name := "ShoppingCartFP"
ThisBuild / version := "0.0.1"

// This exclussion is to supress incorrect warning of inferred Any type with ZIO 2 schedule https://github.com/zio/zio/issues/6645
val excludeInferAny = { options: Seq[String] => options.filterNot(Set("-Xlint:infer-any")) }

lazy val IntegrationTest = config("it") extend(Test)

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "ShoppingCartFPinScala",
    scalaVersion := "2.13.8",
    Compile / mainClass := Some("bunyod.fp.MainIO"),

    Global / onChangedBuildSource := ReloadOnSourceChanges,
    //    scalacOptions ++= CompilerOptions.cOptions,
    //    scalafmtOnCompile := true,
    scalacOptions ++= Seq(
      "-Xfatal-warnings", // New lines for each options
      "-deprecation",
      "-unchecked",
      "-language:implicitConversions",
      "-language:higherKinds",
      "-language:existentials",
      "-language:postfixOps",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfatal-warnings",
      "-deprecation",
    //  "-Xlint:-unused,_",
      "-deprecation",
      "-Ymacro-annotations",
      "-Xmaxerrs",
      "200",
    ),
    Compile / scalacOptions ~= excludeInferAny,
    Test / scalacOptions ~= excludeInferAny,
    dockerBaseImage := "openjdk:8u201-jre-alpine3.9",
    dockerExposedPorts ++= Seq(8080),
    makeBatScripts := Seq(),
    dockerUpdateLatest := true,
    libraryDependencies ++= rootDependencies
  )
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)

