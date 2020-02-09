import java.util.Calendar

name := "mock-server"

organization:= "com.github.mideo"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.github.mideo" %% "api-test-kit-core" % "0.0.5" excludeAll ExclusionRule("org.hamcrest", "java-hamcrest"),
  "com.novocode" % "junit-interface" % "0.11" % "test->default",
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)

assemblyMergeStrategy in assembly := {
  case PathList("apitestkit.properties") => MergeStrategy.rename
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

def executeBashCommand(command:String): Unit ={
  val exitCode: Int = command !

  if (exitCode != 0){
    throw new Exception(s"Failed execution for shell command: `$command`")
  }
}

val awsPackage = TaskKey[Unit]("awsPackage", "Create Deployable AWS Package")

awsPackage := {
  assembly.value

  executeBashCommand("rm -rf dist")
  executeBashCommand("mkdir -p dist")
  executeBashCommand("cp target/scala-2.11/mock-server-assembly-0.1.jar mock-server-assembly-0.1.jar")
  executeBashCommand("zip -r dist/MockServer.zip mocks mock-server-assembly-0.1.jar")
  executeBashCommand("rm mock-server-assembly-0.1.jar")
}