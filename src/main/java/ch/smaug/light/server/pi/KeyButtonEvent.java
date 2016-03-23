package ch.smaug.light.server.pi;

public final class KeyButtonEvent {

	private final Key key;
	private final Edge slope;

	public enum Edge {
		Positive, Negative
	}

	public enum Key {
		Key1, Key2
	}

	public KeyButtonEvent(final Key key, final Edge slope) {
		this.key = key;
		this.slope = slope;
	}

	public Key getKey() {
		return key;
	}

	public Edge getSlope() {
		return slope;
	}
}
