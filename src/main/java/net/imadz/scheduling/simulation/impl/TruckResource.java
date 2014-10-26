package net.imadz.scheduling.simulation.impl;

import net.imadz.lifecycle.LifecycleContext;
import net.imadz.lifecycle.annotations.LifecycleMeta;
import net.imadz.lifecycle.annotations.StateIndicator;
import net.imadz.lifecycle.annotations.Transition;
import net.imadz.lifecycle.annotations.action.Condition;
import net.imadz.lifecycle.annotations.callback.PostStateChange;
import net.imadz.lifecycle.annotations.state.Converter;
import net.imadz.scheduling.simulation.IResource;
import net.imadz.scheduling.simulation.Id;
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle;
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle.Transitions.*;
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle.Conditions.*;

@LifecycleMeta(IResourceLifecycle.class)
public class TruckResource implements IResource, HistoryState {

    @StateIndicator
    @Converter(StateEnumConverter.class)
    private StateEnum state = StateEnum.Idle;
    private StateEnum lastState = StateEnum.Idle;

    @Override
    public Id getId() {
        return null;
    }

    @Override
    public StateEnum getState() {
        return state;
    }

    @Transition(Deploy.class)
    public void doDeploy() {}

    @Transition(Work.class)
    public void doWork() {}

    @Transition(Undeploy.class)
    public void doUndeploy() {}

    @Transition(Release.class)
    public void doRelease() {}

    @Transition(Recover.class)
    public void doResume() {}

    @Transition(Recycle.class)
    public void doRecycle() {}

    @Transition(Fail.class)
    public void doFail() {}

    @Condition(HistoryState.class)
    public HistoryState getHistoryState() {
        return this;
    }

    @Override
    public String getLastState() {
        return lastState.name();
    }

    @PostStateChange
    public void recordOldState(LifecycleContext<TruckResource, StateEnum> context) {
        lastState = context.getFromState();
    }
}
