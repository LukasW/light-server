
package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@RunWith(MockitoJUnitRunner.class)
public class PreOnStateTest extends AbstractStateTest<PreOnState> {

	@InjectMocks
	private final PreOnState testee = new PreOnState();

	@Mock
	private OnState onState;

	@Mock
	private PreMaxState preMaxState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_timeOut_onState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createTimeoutEvent());
		// assert
		assertThat(nextState, is(equalTo(onState)));
	}

	@Test
	public void processEvent_positiveEdge_preMaxState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createPositiveEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(preMaxState)));
	}

	@Test
	public void onEnter_startStartingTimeout() {
		// arrange
		// act
		testee.onEnter();
		// verify
		assertSendDeferredEvent(TEST_STARTING_TIMEOUT, LightStateInputEvent.createTimeoutEvent());
	}

	@Override
	protected PreOnState getTestee() {
		return testee;
	}
}