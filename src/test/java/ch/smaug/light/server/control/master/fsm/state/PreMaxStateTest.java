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
public class PreMaxStateTest extends AbstractStateTest<PreMaxState> {

	@InjectMocks
	private final PreMaxState testee = new PreMaxState();

	@Mock
	private OnState onState;

	@Mock
	private DimState dimState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_timeOut_dimState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.Timeout);
		// assert
		assertThat(nextState, is(equalTo(dimState)));
	}

	@Test
	public void processEvent_negativeEdge_onState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.NegativeEdge);
		// assert
		assertThat(nextState, is(equalTo(onState)));
		verify(masterLightControl).fullLight();
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
	protected PreMaxState getTestee() {
		return testee;
	}
}