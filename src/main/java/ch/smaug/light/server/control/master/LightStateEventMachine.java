package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ch.smaug.light.server.control.master.LightStateEvent.Type;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;

@ApplicationScoped
public class LightStateEventMachine {

	private State state;
	@Inject
	private Event<LightStateEvent> lightStateEvent;

	public LightStateEventMachine() {
		state = State.Off;
	}

	public void processKeyButtonEvent(@Observes final KeyButtonEvent keyButtonEvent) {
		processEvent(keyButtonEvent.getSlope() == Edge.Positive ? InputEvents.PositiveEdge : InputEvents.NegativeEdge);
	}

	private void processEvent(final InputEvents event) {
		switch (state) {
		case Off:
			switch (event) {
			case NegativeEdge:
				state = State.On;
				lightStateEvent.fire(new LightStateEvent(Type.TurnOn));
				break;
			case PositiveEdge:
				break;
			}
			break;
		case On:
			switch (event) {
			case NegativeEdge:
				state = State.Off;
				lightStateEvent.fire(new LightStateEvent(Type.TurnOff));
				break;
			case PositiveEdge:
				break;
			}
			break;
		}
	}

	public enum InputEvents {
		NegativeEdge, PositiveEdge
	}

	enum State {
		Off, On
	}

	void setState(final State state) {
		this.state = state;
	}
}
