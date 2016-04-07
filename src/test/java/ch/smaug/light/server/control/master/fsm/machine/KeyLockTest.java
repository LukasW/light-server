package ch.smaug.light.server.control.master.fsm.machine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;

public class KeyLockTest {

	private final KeyLock testee = new KeyLock();

	@Test
	public void acquireLock_noLockSet_lockIsSet() {
		// arrange
		// act
		testee.acquireLock("A");
		final Optional<String> lockedKey = testee.getLockedKey();
		// assert
		assertThat(lockedKey, is(equalTo(Optional.of("A"))));
	}

	@Test
	public void releaseLock_lockSet_lockIsReleased() {
		// arrange
		testee.acquireLock("A");
		// act
		testee.releaseLock();
		final Optional<String> lockedKey = testee.getLockedKey();
		// assert
		assertThat(lockedKey, is(equalTo(Optional.empty())));
	}

	@Test(expected = IllegalArgumentException.class)
	public void acquireLock_lockIsSet_exception() {
		// arrange
		testee.acquireLock("A");
		// act
		testee.acquireLock("B");
	}
}
