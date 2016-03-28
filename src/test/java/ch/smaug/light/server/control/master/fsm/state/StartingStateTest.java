
package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.DelayedEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateOutputEvent.Type;

@RunWith(CdiRunner.class)
public class StartingStateTest {

	@Inject
	private StartingState testee;

	@Inject
	private DimDownState dimDownState;

	@Mock
	@Produces
	private MasterLightControl masterLightControl;

	@Mock
	@Produces
	private DelayedEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	private LightStateOutputEvent lastLightStateOutputEvent;

	@Test
	public void processEvent_timeOut() {
		// arrange
		// act
		final State nextState = testee.process(new LightStateInputEvent(InputEventType.Timeout));
		// assert
		verify(delayedLightStateInputEventSender).startTimer(Matchers.anyLong(), Matchers.any(LightStateInputEvent.class));
		assertThat(lastLightStateOutputEvent.getType(), is(equalTo(Type.Previous)));
		assertThat(nextState, is(equalTo(dimDownState)));
	}

	public void foo(@Observes final LightStateOutputEvent lightStateOutputEvent) {
		this.lastLightStateOutputEvent = lightStateOutputEvent;
	}
}