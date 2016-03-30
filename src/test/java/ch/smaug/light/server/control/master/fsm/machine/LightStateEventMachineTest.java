package ch.smaug.light.server.control.master.fsm.machine;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalPackages;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.cdi.TestConfigValueProducer;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;
import ch.smaug.light.server.control.master.fsm.state.OffState;
import ch.smaug.light.server.control.master.fsm.state.OnState;

@RunWith(CdiRunner.class)
@AdditionalPackages(OffState.class)
@ActivatedAlternatives(TestConfigValueProducer.class)
public class LightStateEventMachineTest {

	@Inject
	private TestConfigValueProducer testConfigValueProducer;
	@Inject
	private LightStateEventMachine testee;

	@Inject
	private Instance<Object> instance;

	@Mock
	@Produces
	private DeferredEvent<LightStateInputEvent> delayLightStateInputEventSender;

	private final List<LightStateOutputEvent> lightStateEvents = new LinkedList<>();

	@Before
	public void setDefaultConfigValues() {
		testConfigValueProducer.setConfigValue("startingTimeout", 0L);
		testConfigValueProducer.setConfigValue("repeatingTimeout", 0L);
	}

	@Test
	public void turnLightOn() {
		// Arrange
		// Act
		testee.processEvent(LightStateInputEvent.PositiveEdge);
		testee.processEvent(LightStateInputEvent.NegativeEdge);
		// Assert
		assertThat(getLastLightStateEvent(), is(equalTo(LightStateOutputEvent.TurnOn)));
	}

	@Test
	public void turnLightOff() {
		// Arrange
		testee.setState(instance.select(OnState.class).get());
		// Act
		testee.processEvent(LightStateInputEvent.PositiveEdge);
		testee.processEvent(LightStateInputEvent.NegativeEdge);
		// Assert
		assertThat(getLastLightStateEvent(), is(equalTo(LightStateOutputEvent.TurnOff)));
	}

	@Test
	public void turnLightOn_dimDown() {
		// Arrange
		// Act
		testee.processEvent(LightStateInputEvent.PositiveEdge);
		testee.processEvent(LightStateInputEvent.Timeout);
		testee.processEvent(LightStateInputEvent.NegativeEdge);
		// Assert
		final List<LightStateOutputEvent> expectedEvents = Arrays.asList(LightStateOutputEvent.TurnOn, LightStateOutputEvent.DimDown);
		assertThat(lightStateEvents, is(equalTo(expectedEvents)));
	}

	public void consume(@Observes final LightStateOutputEvent lightStateEvent) {
		lightStateEvents.add(lightStateEvent);
	}

	private LightStateOutputEvent getLastLightStateEvent() {
		return lightStateEvents.get(lightStateEvents.size() - 1);
	}
}
