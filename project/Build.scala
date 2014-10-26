import sbt.Keys._
import sbt._

object BuildApp extends Build {


  lazy val weaveClassImpl: Def.Initialize[Task[Unit]] = Def.task[Unit] {

    val classpath = fullClasspath.in(Compile).result.value match {
      case Value(files) =>
        files.map(file => file.data.getAbsolutePath).mkString(":")
      case _ => ""
    }

    import scala.sys.process._

    val cmd = "java -cp " + classpath + " -javaagent:" + "./lib/Lifecycle-0.9.2-SNAPSHOT.jar" + " -Dnet.imadz.bcel.save.original=true" + " " + "net.imadz.lifecycle.StaticWeaver " + "./target/scala-2.11/classes" !!;

    println(cmd)

  } dependsOn (compile in Compile)

}