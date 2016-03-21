package ch.smaug.light.server.control.fading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FadingThread extends Thread {

	private final static Logger LOG = LoggerFactory.getLogger(FadingThread.class);

	private int actualLevel;
	private int expectedLevel;

	public FadingThread() {
		super("Fading Thread");
		setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (this) {
					while (actualLevel == expectedLevel) {
						wait();
					}
				}
				step();
				Thread.sleep(10);
			}
		} catch (final InterruptedException e) {
			// Terminating
		}
	}

	public synchronized void setExpectedLevel(final int expectedLevel) {
		this.expectedLevel = expectedLevel;
		if (this.expectedLevel != actualLevel) {
			notify();
		}
	}

	private synchronized void step() {
		final int direction = Integer.signum(expectedLevel - actualLevel);
		actualLevel += direction;
		setActualLevel(actualLevel);
		LOG.debug("Set actual level to {}.", actualLevel);
	}

	protected abstract void setActualLevel(int actualLevel);
}
