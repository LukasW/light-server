package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;

import ch.smaug.light.server.control.master.fsm.DelayedEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;

@RunWith(CdiRunner.class)
public class OffStateTest {

	@Inject
	private OffState testee;

	@Inject
	private StartingState startingState;

	@Mock
	@Produces
	private DelayedEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	@Test
	public void processEvent_positiveEvent() {
		// arrange
		// act
		final State nextState = testee.process(new LightStateInputEvent(InputEventType.PositiveEdge));
		// assert
		verify(delayedLightStateInputEventSender).startTimer(Matchers.anyLong(), Matchers.any(LightStateInputEvent.class));
		assertThat(nextState, is(equalTo(startingState)));
	}

	@Test
	public void processEvent_negativeEvent() {
		// arrange
		// act
		final State nextState = testee.process(new LightStateInputEvent(InputEventType.NegativeEdge));
		// assert
		assertThat(nextState, is(equalTo(testee)));
	}
}
