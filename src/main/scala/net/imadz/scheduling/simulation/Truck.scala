package net.imadz.scheduling.simulation

import net.imadz.lifecycle.LifecycleContext
import net.imadz.lifecycle.annotations.action.Condition
import net.imadz.lifecycle.annotations.callback.PostStateChange
import net.imadz.lifecycle.annotations.state.Converter
import net.imadz.lifecycle.annotations.{LifecycleMeta, StateIndicator, Transition}
import net.imadz.scheduling.simulation.IResource.StateEnum
import net.imadz.scheduling.simulation.impl.{StateEnumConverter, TruckResource}
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle.Conditions.HistoryState
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle.Transitions._

/**
 * Created by geek on 10/26/14.
 */
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
