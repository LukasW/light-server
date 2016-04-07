package ch.smaug.light.server.control.master.fsm.machine;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeyLock {

	private Optional<String> keyName = Optional.empty();

	public Optional<String> getLockedKey() {
		return keyName;
	}

	public void acquireLock(final String keyName) {
		if (this.keyName.isPresent()) {
			throw new IllegalArgumentException(String.format("Key %s is already locked (request was %s}).", this.keyName.get(), keyName));
		}
		this.keyName = Optional.of(keyName);
	}

	public void releaseLock() {
		keyName = Optional.empty();
	}

}
