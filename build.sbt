name := "akka-iot-mqtt"

version := "0.1"

scalaVersion := "2.11.7"
lazy val akkaVersion = "2.4.0"

fork in Test := true

libraryDependencies ++= Seq(
  "org.eclipse.paho"  % "org.eclipse.paho.client.mqttv3"  % "1.0.2",
  "com.sandinh"  % "paho-akka_2.11"  % "1.2.0",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback"    % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "commons-io" % "commons-io" % "2.4" % "test")


fork in run := true
