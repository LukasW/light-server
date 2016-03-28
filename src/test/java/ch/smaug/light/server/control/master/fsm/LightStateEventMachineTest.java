package ch.smaug.light.server.control.master.fsm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jglue.cdiunit.AdditionalPackages;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent.Type;
import ch.smaug.light.server.control.master.fsm.state.OffState;
import ch.smaug.light.server.control.master.fsm.state.OnState;

@RunWith(CdiRunner.class)
@AdditionalPackages(OffState.class)
public class LightStateEventMachineTest {

	@Inject
	private LightStateEventMachine testee;
	@Inject
	private Instance<Object> instance;

	private final List<LightStateOutputEvent> lightStateEvents = new LinkedList<>();

	@Test
	public void turnLightOn() {
		// Arrange
		// Act
		testee.processEvent(new LightStateInputEvent(InputEventType.PositiveEdge));
		testee.processEvent(new LightStateInputEvent(InputEventType.NegativeEdge));
		// Assert
		assertThat(getLastLightStateEvent().getType(), is(equalTo(Type.TurnOn)));
	}

	@Test
	public void turnLightOff() {
		// Arrange
		testee.setState(instance.select(OnState.class).get());
		// Act
		testee.processEvent(new LightStateInputEvent(InputEventType.PositiveEdge));
		testee.processEvent(new LightStateInputEvent(InputEventType.NegativeEdge));
		// Assert
		assertThat(getLastLightStateEvent().getType(), is(equalTo(Type.TurnOff)));
	}

	@Test
	public void turnLightOn_dimDown() {
		// Arrange
		// Act
		testee.processEvent(new LightStateInputEvent(InputEventType.PositiveEdge));
		testee.processEvent(new LightStateInputEvent(InputEventType.Timeout));
		testee.processEvent(new LightStateInputEvent(InputEventType.NegativeEdge));
		// Assert
		final List<LightStateOutputEvent> expectedEvents = Arrays.asList(new LightStateOutputEvent(Type.TurnOn),
				new LightStateOutputEvent(Type.Previous));
		assertThat(lightStateEvents, is(equalTo(expectedEvents)));
	}

	public void consume(@Observes final LightStateOutputEvent lightStateEvent) {
		lightStateEvents.add(lightStateEvent);
	}

	private LightStateOutputEvent getLastLightStateEvent() {
		return lightStateEvents.get(lightStateEvents.size() - 1);
	}
}
