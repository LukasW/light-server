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
public class OffStateTest extends AbstractStateTest<OffState> {

	@InjectMocks
	private final OffState testee = new OffState();

	@Mock
	private StartingState startingState;

	@Mock
	private MasterLightControl masterLightControl;

	@Test
	public void processEvent_positiveEdge_startingState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.PositiveEdge);
		// assert
		assertThat(nextState, is(equalTo(startingState)));
	}

	@Test
	public void onEnter_turnLightOff() {
		// arrange
		// act
		testee.onEnter();
		// assert
		verify(masterLightControl).turnOff();
	}

	@Override
	protected OffState getTestee() {
		return testee;
	}
}
