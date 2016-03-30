package ch.smaug.light.server.cdi;

import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class DeferredEventQueue {

	private final Set<TimerTask> queue = new HashSet<>();

	public synchronized void cancelAll() {
		for (final TimerTask timerTask : queue) {
			timerTask.cancel();
		}
	}

	public synchronized void add(final TimerTask timerTask) {
		queue.add(timerTask);
	}

	public synchronized void remove(final TimerTask timerTask) {
		queue.remove(timerTask);
	}

	public synchronized int size() {
		return queue.size();
	}
}
