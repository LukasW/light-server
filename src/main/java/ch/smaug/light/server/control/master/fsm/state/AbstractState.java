package ch.smaug.light.server.control.master.fsm.state;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.cdi.ConfigValue;
import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;

public abstract class AbstractState {

	private final static Logger LOG = LoggerFactory.getLogger(AbstractState.class);

	@Inject
	private DeferredEvent<LightStateInputEvent> delayedLightStateInputEventSender;

	@Inject
	@ConfigValue("repeatingTimeout")
	private Long repeatingTimeout;

	@Inject
	@ConfigValue("startingTimeout")
	private Long startingTimeout;

	public abstract AbstractState process(LightStateInputEvent event);

	public void enter() {
		LOG.debug("[{}] Enter state", getName());
		onEnter();
	}

	public void exit() {
		LOG.debug("[{}] Leaving state", getName());
		onExit();
	}

	public String getName() {
		return getClass().getSimpleName();
	}

	protected void onEnter() {
	}

	protected void onExit() {
		delayedLightStateInputEventSender.cancelAll();
	}

	public void sendStartingTimeout() {
		delayedLightStateInputEventSender.sendDeferred(startingTimeout, LightStateInputEvent.Timeout);
	}

	public void sendRepeatingTimeout() {
		delayedLightStateInputEventSender.sendDeferred(repeatingTimeout, LightStateInputEvent.Timeout);
	}

	protected DeferredEvent<LightStateInputEvent> getDelayedLightStateInputEventSender() {
		return delayedLightStateInputEventSender;
	}
}
