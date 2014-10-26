package net.imadz.scheduling.simulation.impl;

import net.imadz.lifecycle.StateConverter;
import net.imadz.scheduling.simulation.IResource.StateEnum;

public class StateEnumConverter implements StateConverter<StateEnum> {

	@Override
	public StateEnum fromState(String stateName) {
		return StateEnum.valueOf(stateName);
	}

	@Override
	public String toState(StateEnum state) {
		return state.name();
	}

}
