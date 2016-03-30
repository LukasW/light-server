package ch.smaug.light.server.cdi;

import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeferredEvent<T> {

	private final static Logger LOG = LoggerFactory.getLogger(DeferredEvent.class);

	@Inject
	private Instance<Event<T>> eventSender;

	@Inject
	private DeferredEventQueue deferredEventQueue;

	private final Timer timer = new Timer(true);

	public void sendDeferred(final long delay, final T event) {
		LOG.debug("Delayed event {} for {} ms.", event, delay);
		final Exception start = new Exception();
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				LOG.debug("Send delayed event {}.", event);
				try {
					eventSender.get().fire(event);
				} catch (final ContextNotActiveException e) {
					LOG.error("Error while sending " + event, start);
				}
				deferredEventQueue.remove(this);
			}
		};
		deferredEventQueue.add(timerTask);
		timer.schedule(timerTask, delay);
	}

	public void cancelAll() {
		LOG.debug("Cancel all deferred events: {} in queue.", deferredEventQueue.size());
		deferredEventQueue.cancelAll();
	}
}
