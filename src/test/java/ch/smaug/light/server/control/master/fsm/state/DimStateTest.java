package ch.smaug.light.server.control.master.fsm.state;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

@RunWith(MockitoJUnitRunner.class)
public class DimStateTest extends AbstractStateTest<DimState> {

	@InjectMocks
	private final DimState testee = new DimState();

	@Mock
	private OnState onState;

	@Mock
	private OffState offState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_timeOut_dimState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createTimeoutEvent());
		// assert
		assertThat(nextState, is(equalTo(testee)));
	}

	@Test
	public void processEvent_negativeEdgeAndLightIsOn_onState() {
		// arrange
		doReturn(false).when(masterLightControl).isOff();
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(onState)));
	}

	@Test
	public void processEvent_negativeEdgeAndLightIsOff_offState() {
		// arrange
		doReturn(true).when(masterLightControl).isOff();
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// assert
		assertThat(nextState, is(equalTo(offState)));
	}

	@Test
	public void onEnter_startStartingTimeoutAndDim() {
		// arrange
		// act
		testee.onEnter();
		// verify
		assertSendDeferredEvent(TEST_REPEATING_TIMEOUT, LightStateInputEvent.createTimeoutEvent());
		verify(masterLightControl).dim();
	}

	@Override
	protected DimState getTestee() {
		return testee;
	}
}
