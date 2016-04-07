package ch.smaug.light.server.control.master.fsm.machine;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.state.AbstractState;

@RunWith(MockitoJUnitRunner.class)
public class LightStateEventMachineTest {

	@InjectMocks
	private final LightStateEventMachine testee = new LightStateEventMachine();

	@Mock
	private AbstractState startingState;

	@Mock
	private AbstractState endingState;

	@Test
	public void processEvent_callbackMethodsAreCalled() {
		// arrange
		testee.setState(startingState);
		doReturn(endingState).when(startingState).process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// act
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// assert
		verify(startingState).exit();
		verify(startingState).process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		verify(endingState).enter();
	}

	@Test
	public void processEvent_unexpectedEvent() {
		// arrange
		testee.setState(startingState);
		// act
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		// assert
		verify(startingState).exit();
		verify(startingState).process(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
		verify(startingState).enter();
	}
}
