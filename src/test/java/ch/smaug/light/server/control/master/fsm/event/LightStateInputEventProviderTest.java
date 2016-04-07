package ch.smaug.light.server.control.master.fsm.event;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import javax.enterprise.event.Event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.master.fsm.machine.KeyLock;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;

@RunWith(MockitoJUnitRunner.class)
public class LightStateInputEventProviderTest {

	@Mock
	private Event<LightStateInputEvent> lightStateInputEvent;

	@Mock
	private KeyLock keyLock;

	@InjectMocks
	private final LightStateInputEventProvider testee = new LightStateInputEventProvider();

	@Test
	public void processKeyButtonEvent_positiveEdge_firePositiveEdge() {
		// arrange
		doReturn(Optional.empty()).when(keyLock).getLockedKey();
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Positive));
		// assert
		verify(lightStateInputEvent).fire(LightStateInputEvent.createPositiveEdgeEvent("Key1"));
	}

	@Test
	public void processKeyButtonEvent_negativeEdge_fireNegativeEdge() {
		// arrange
		doReturn(Optional.empty()).when(keyLock).getLockedKey();
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Negative));
		// assert
		verify(lightStateInputEvent).fire(LightStateInputEvent.createNegativeEdgeEvent("Key1"));
	}

	@Test
	public void processKeyButtonEvent_lockedForOtherKey_ignoreMessage() {
		// arrange
		doReturn(Optional.of(Key.Key1.name())).when(keyLock).getLockedKey();
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key2, Edge.Negative));
		// assert
		verify(lightStateInputEvent, never()).fire(Matchers.any(LightStateInputEvent.class));
	}

	@Test
	public void processKeyButtonEvent_lockedForSameKey_messageIsFired() {
		// arrange
		doReturn(Optional.of(Key.Key1.name())).when(keyLock).getLockedKey();
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key2, Edge.Negative));
		// assert
		verify(lightStateInputEvent, never()).fire(Matchers.any(LightStateInputEvent.class));
	}
}
