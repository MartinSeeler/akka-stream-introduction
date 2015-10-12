name := "akka-streams-introduction"
version := "1.0"
scalaVersion := "2.11.7"

val akkaStreamV = "1.0"
val akkaHttpV = "1.0"
val scalaTestV = "2.2.5"

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-stream-experimental"          % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-experimental"            % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaHttpV,

  "org.scalatest"     %% "scalatest"                         % scalaTestV   % "test",
  "com.typesafe.akka" %% "akka-stream-testkit-experimental"  % akkaStreamV  % "test",
  "com.typesafe.akka" %% "akka-http-testkit-experimental"    % akkaHttpV    % "test"
)
