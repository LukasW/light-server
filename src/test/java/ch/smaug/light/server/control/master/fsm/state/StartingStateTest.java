
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
import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

@RunWith(CdiRunner.class)
@ActivatedAlternatives(TestConfigValueProducer.class)
public class StartingStateTest {

	@Inject
	private TestConfigValueProducer testConfigValueProducer;

	@Inject
	private StartingState testee;

	@Inject
	private DimDownState dimDownState;

	@Mock
	@Produces
	private MasterLightControl masterLightControl;

	@Mock
	@Produces
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	private LightStateOutputEvent lastLightStateOutputEvent;

	@Test
	public void processEvent_timeOut() {
		// arrange
		testConfigValueProducer.setConfigValue("repeatingTimeout", 88L);
		// act
		final State nextState = testee.process(LightStateInputEvent.Timeout);
		// assert
		verify(delayedLightStateInputEventSender).sendDeferred(88, LightStateInputEvent.Timeout);
		assertThat(lastLightStateOutputEvent, is(equalTo(LightStateOutputEvent.DimDown)));
		assertThat(nextState, is(equalTo(dimDownState)));
	}

	public void processLightStateOutputEvent(@Observes final LightStateOutputEvent lightStateOutputEvent) {
		this.lastLightStateOutputEvent = lightStateOutputEvent;
	}
}