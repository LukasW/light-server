package ch.smaug.light.server.control.master;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.master.LightStateEvent.Type;
import ch.smaug.light.server.control.master.LightStateEventMachine.State;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;

@RunWith(CdiRunner.class)
public class LightStateEventMachineTest {

	@Inject
	private LightStateEventMachine testee;
	private LightStateEvent lastLightStateEvent;

	@Test
	public void event_stateOff_positiveAndNegativeEdge_turnLightOn() {
		// Arrange
		// Act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Positive));
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Negative));
		// Assert
		assertThat(lastLightStateEvent.getType(), is(equalTo(Type.TurnOn)));
	}

	@Test
	public void event_stateOn_PositiveAndNegativeEdge_turnLightOff() {
		// Arrange
		testee.setState(State.On);
		// Act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Positive));
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Negative));
		// Assert
		assertThat(lastLightStateEvent.getType(), is(equalTo(Type.TurnOff)));
	}

	public void consume(@Observes final LightStateEvent lightStateEvent) {
		this.lastLightStateEvent = lightStateEvent;
	}

}
