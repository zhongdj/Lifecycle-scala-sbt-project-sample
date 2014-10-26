package net.imadz.scheduling.simulation;

import net.imadz.lifecycle.annotations.Transition;
import net.imadz.scheduling.simulation.lifecycle.IResourceLifecycle.Transitions.*;

public interface IResource {

    Id getId();

    StateEnum getState();

    public static enum StateEnum {
        Idle,
        Deploying,
        Working,
        Undeploying,
        Failing,
        Interrupting,
        Recycled
    }

    @Transition(Deploy.class)
    void doDeploy();

    @Transition(Work.class)
    void doWork();

    @Transition(Undeploy.class)
    void doUndeploy();

    @Transition(Release.class)
    void doRelease();

    @Transition(Recover.class)
    void doResume();

    @Transition(Recycle.class)
    void doRecycle();

    @Transition(Fail.class)
    void doFail();
}
