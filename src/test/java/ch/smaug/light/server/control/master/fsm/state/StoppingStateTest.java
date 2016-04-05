package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@RunWith(MockitoJUnitRunner.class)
public class StoppingStateTest extends AbstractStateTest<StoppingState> {

	@InjectMocks
	private final StoppingState testee = new StoppingState();

	@Mock
	private OffState offState;

	@Mock
	private DimState dimState;

	@Test
	public void processEvent_timeOut_dimUpStateAndSendDimRequest() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.Timeout);
		// assert
		assertThat(nextState, is(equalTo(dimState)));
	}

	@Test
	public void processEvent_negativeEdge_offState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.NegativeEdge);
		// assert
		assertThat(nextState, is(equalTo(offState)));
	}

	@Test
	public void onEnter_startStartingTimeout() {
		// arrange
		// act
		testee.onEnter();
		// verify
		assertSendDeferredEvent(TEST_STARTING_TIMEOUT, LightStateInputEvent.Timeout);
	}

	@Override
	protected StoppingState getTestee() {
		return testee;
	}
}
