package ch.smaug.light.server.control.master.fsm;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayedEvent<T> {

	private final static Logger LOG = LoggerFactory.getLogger(DelayedEvent.class);

	@Inject
	private Instance<Event<T>> eventSender;

	private final Timer timer = new Timer(false);
	private final Set<TimerTask> queue = new HashSet<>();

	public void startTimer(final long delay, final T event) {
		LOG.debug("Delayed event {} for {} ms.", event, delay);
		final TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				synchronized (queue) {
					eventSender.get().fire(event);
					queue.remove(this);
				}
			}
		};
		synchronized (queue) {
			queue.add(timerTask);
		}
		timer.schedule(timerTask, delay);
	}

	public void stopTimer() {
		synchronized (queue) {
			for (final TimerTask timerTask : queue) {
				timerTask.cancel();
			}
		}
	}
}
