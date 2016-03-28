package ch.smaug.light.server.control.master.fsm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class DelayedEventTest {

	@Inject
	private DelayedEvent<String> testee;
	private final Semaphore semaphore = new Semaphore(0);
	private String lastTestEvent;

	@Test
	public void startTimer_sendAndReceive() throws InterruptedException {
		// arrange
		// act
		testee.startTimer(5L, "Hello");
		semaphore.tryAcquire(50L, TimeUnit.MILLISECONDS);
		// verify
		assertThat(lastTestEvent, is(equalTo("Hello")));
	}

	@Test
	public void stopTimer_nothingSent() throws InterruptedException {
		// arrange
		// act
		testee.startTimer(5L, "Hello");
		testee.stopTimer();
		final boolean taken = semaphore.tryAcquire(50L, TimeUnit.MILLISECONDS);
		// verify
		assertThat(taken, is(equalTo(false)));
	}

	public void processEvent(@Observes final String testEvent) {
		this.lastTestEvent = testEvent;
		semaphore.release();
	}
}
