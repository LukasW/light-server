package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

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

@RunWith(CdiRunner.class)
@ActivatedAlternatives(TestConfigValueProducer.class)
public class OnStateTest {

	@Inject
	private TestConfigValueProducer testConfigValueProducer;

	@Inject
	private OnState testee;

	@Inject
	private PressedState pressedState;

	@Mock
	@Produces
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	@Test
	public void processEvent_positiveEvent() {
		// arrange
		testConfigValueProducer.setConfigValue("startingTimeout", 19L);
		// act
		final State nextState = testee.process(LightStateInputEvent.PositiveEdge);
		// assert
		verify(delayedLightStateInputEventSender).sendDeferred(19, LightStateInputEvent.Timeout);
		assertThat(nextState, is(equalTo(pressedState)));
	}
}
