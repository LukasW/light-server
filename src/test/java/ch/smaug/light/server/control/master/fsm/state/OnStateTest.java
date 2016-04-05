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
public class OnStateTest extends AbstractStateTest<OnState> {

	@InjectMocks
	private final OnState testee = new OnState();

	@Mock
	private StoppingState stoppingState;

	@Test
	public void processEvent_positiveEdge_stoppingState() {
		// arrange
		// act
		final AbstractState nextState = testee.process(LightStateInputEvent.PositiveEdge);
		// assert
		assertThat(nextState, is(equalTo(stoppingState)));
	}

	@Override
	protected OnState getTestee() {
		return testee;
	}
}