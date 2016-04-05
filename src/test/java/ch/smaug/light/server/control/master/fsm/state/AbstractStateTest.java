package ch.smaug.light.server.control.master.fsm.state;

import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

public abstract class AbstractStateTest<T extends AbstractState> {

	protected static final Long TEST_REPEATING_TIMEOUT = 10L;
	protected static final Long TEST_STARTING_TIMEOUT = 100L;

	@Mock
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	@Before
	public void injectConfigurationValues() {
		injectConfigValue("startingTimeout", TEST_STARTING_TIMEOUT);
		injectConfigValue("repeatingTimeout", TEST_REPEATING_TIMEOUT);
	}

	@Test
	public void onExit_cancelTimer() {
		// arrange
		// act
		getTestee().onExit();
		// verify
		verify(delayedLightStateInputEventSender).cancelAll();
	}

	protected void assertSendDeferredEvent(final Long timeout, final LightStateInputEvent event) {
		verify(delayedLightStateInputEventSender).sendDeferred(timeout, event);
	}

	protected abstract T getTestee();

	private void injectConfigValue(final String configValue, final Long value) {
		try {
			final Field prop = AbstractState.class.getDeclaredField(configValue);
			prop.setAccessible(true);
			prop.set(getTestee(), value);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
