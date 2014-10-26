This project demonstrate how to use Lifecycle with sbt to weave both Java and Scala classes.

#Configure sbt build section#

1. create a Task (sbt term) to weave the binary class file (byte code). In this example we use project/Build.scala to define the task.

```
  lazy val weaveClassImpl: Def.Initialize[Task[Unit]] = Def.task[Unit] {

    val classpath = fullClasspath.in(Compile).result.value match {
      case Value(files) => files.map(file => file.data.getAbsolutePath).mkString(":")
      case _ => ""
    }

    import scala.sys.process._

    val cmd = "java -cp " + classpath + " -javaagent:" + "./lib/Lifecycle-0.9.2-SNAPSHOT.jar" + " -Dnet.imadz.bcel.save.original=true" + " " + "net.imadz.lifecycle.StaticWeaver " + "./target/scala-2.11/classes" !!;

    println(cmd)

  } dependsOn (compile in Compile)
```

2. append weave task into packageBin, package and test tasks in build.sbt file

```
packageBin <<= packageBin in Compile dependsOn BuildApp.weaveClassImpl

sbt.Keys.`package` in Compile <<= sbt.Keys.`package` in Compile dependsOn BuildApp.weaveClassImpl

test <<= test in Test dependsOn BuildApp.weaveClassImpl

```

#Lifecycle Definition#
See src/main/java/net/imadz/scheduling/simulation/lifecycle/IResourceLifecycle.java

#Java Version Lifecycle Implementation Sample#
See src/main/java/net/imadz/scheduling/simulation/impl/TruckResource.java

#Scala Version Lifecycle Implementation Sample#
```
@LifecycleMeta(classOf[IResourceLifecycle])
case class Truck(id: Id) extends IResource with HistoryState {

  @StateIndicator
  @Converter(classOf[StateEnumConverter])
  private var state = StateEnum.Idle

  private var lastState = StateEnum.Idle

  override def getId: Id = id

  @Transition(classOf[Release])
  override def doRelease(): Unit = {}

  @Transition(classOf[Work])
  override def doWork(): Unit = {}

  @Transition(classOf[Recover])
  override def doResume(): Unit = {}

  @Transition(classOf[Recycle])
  override def doRecycle(): Unit = {}

  @Transition(classOf[Fail])
  override def doFail(): Unit = {}


  @Transition(classOf[Deploy])
  override def doDeploy(): Unit = {}

  @Transition(classOf[Undeploy])
  override def doUndeploy(): Unit = {}

  override def getState: StateEnum = state
  override def getLastState: String = lastState.name

  @Condition(classOf[IResourceLifecycle.Conditions.HistoryState])
  def getHistoryState: IResourceLifecycle.Conditions.HistoryState = this

  @PostStateChange
  def recordOldState(context: LifecycleContext[TruckResource, IResource.StateEnum]) {
    lastState = context.getFromState
  }
}
```

#Test with JUnit#
We prefer to use scala test frameworks, but if your test code were build on top of junit, we can make it work as following:

1. Add lib dependencies in build.sbt
```
libraryDependencies += "junit" % "junit" % "4.9" % Test

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
```

2. Add some JUnit tests as
```
public class TruckResourceTest {

    @Test
    public void truckResource_should_automatic_change_state_and_correctly_resume_to_undeploying_state_with_conditional_transition_recover_on_Java_class() {
        TruckResource t = new TruckResource();
        t.doDeploy();
        assertEquals(StateEnum.Deploying, t.getState());
        t.doWork();
        assertEquals(StateEnum.Working, t.getState());
        t.doUndeploy();
        assertEquals(StateEnum.Undeploying, t.getState());
        t.doFail();
        assertEquals(StateEnum.Failing, t.getState());
        t.doResume();
        assertEquals(StateEnum.Undeploying, t.getState());
    }

    @Test
    public void truckResource_should_automatic_change_state_and_correctly_resume_to_undeploying_state_with_conditional_transition_recover_on_scala_class() {
        Truck t = Truck.apply(new Id() {});
        t.doDeploy();
        assertEquals(StateEnum.Deploying, t.getState());
        t.doWork();
        assertEquals(StateEnum.Working, t.getState());
        t.doUndeploy();
        assertEquals(StateEnum.Undeploying, t.getState());
        t.doFail();
        assertEquals(StateEnum.Failing, t.getState());
        t.doResume();
        assertEquals(StateEnum.Undeploying, t.getState());
    }
}

```

3. Use sbt console to perform tests
```
sbt test

...
[info] Compiling 1 Java source to /Users/geek/Workspaces/sbtexample/target/scala-2.11/test-classes...
[info] Passed: Total 2, Failed 0, Errors 0, Passed 2
[success] Total time: 6 s, completed Oct 26, 2014 9:44:15 PM

```