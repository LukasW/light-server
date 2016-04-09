
package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@RunWith(MockitoJUnitRunner.class)
public class PreOffStateTest extends AbstractStateTest<PreOffState> {

	@InjectMocks
	private final PreOffState testee = new PreOffState();

	@Mock
	private OffState offState;

	@Mock
	private PreMaxState preMaxState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_timeOut_offState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createTimeoutEvent());
		// assert
		assertThat(nextState, is(equalTo(offState)));
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
	public void onEnter_startStartingTimeoutAndTurnsLightOff() {
		// arrange
		// act
		testee.onEnter();
		// verify
		assertSendDeferredEvent(TEST_STARTING_TIMEOUT, LightStateInputEvent.createTimeoutEvent());
		verify(masterLightControl).turnOff();
	}

	@Override
	protected PreOffState getTestee() {
		return testee;
	}
}