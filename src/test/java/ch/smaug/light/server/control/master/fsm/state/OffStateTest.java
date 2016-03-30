package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.cdi.TestConfigValueProducer;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

@RunWith(CdiRunner.class)
@ActivatedAlternatives(TestConfigValueProducer.class)
public class OffStateTest {

	@Inject
	private TestConfigValueProducer testConfigValueProducer;

	@Inject
	private OffState testee;

	@Inject
	private StartingState startingState;

	@Mock
	@Produces
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	private LightStateOutputEvent lastLightStateOutputEvent;

	@Test
	public void processEvent_positiveEdge() {
		// arrange
		testConfigValueProducer.setConfigValue("startingTimeout", 7L);
		// act
		final State nextState = testee.process(LightStateInputEvent.PositiveEdge);
		// assert
		verify(delayedLightStateInputEventSender).sendDeferred(7, LightStateInputEvent.Timeout);
		assertThat(nextState, is(equalTo(startingState)));
		assertThat(lastLightStateOutputEvent, is(equalTo(LightStateOutputEvent.TurnOn)));
	}

	public void processLightStateOutputEvent(@Observes final LightStateOutputEvent lightStateOutputEvent) {
		this.lastLightStateOutputEvent = lightStateOutputEvent;
	}

}