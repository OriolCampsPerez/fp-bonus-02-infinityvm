name := "infinityvm"
scalaVersion := "3.1.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-language:higherKinds",
  "-language:implicitConversions",
)

resolvers += Resolver.sonatypeRepo("releases")

// Disallow some language construcs
// your bonus exercises will have to compile with these options
addCompilerPlugin("org.wartremover" %% "wartremover" % "3.0.2" cross CrossVersion.full)
scalacOptions ++= Seq(
  "-P:wartremover:traverser:org.wartremover.warts.AsInstanceOf",
  "-P:wartremover:traverser:org.wartremover.warts.IsInstanceOf",
  "-P:wartremover:traverser:org.wartremover.warts.MutableDataStructures",
  "-P:wartremover:traverser:org.wartremover.warts.Null",
  "-P:wartremover:traverser:org.wartremover.warts.Return",
  "-P:wartremover:traverser:org.wartremover.warts.Throw",
  "-P:wartremover:traverser:org.wartremover.warts.Var",
  "-P:wartremover:traverser:org.wartremover.warts.While",
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
