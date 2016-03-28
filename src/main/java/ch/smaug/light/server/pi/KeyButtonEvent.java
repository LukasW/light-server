package ch.smaug.light.server.pi;

public final class KeyButtonEvent {

	private final Key key;
	private final Edge edge;

	public KeyButtonEvent(final Key key, final Edge edge) {
		this.key = key;
		this.edge = edge;
	}

	public Key getKey() {
		return key;
	}

	public Edge getEdge() {
		return edge;
	}

	public enum Edge {
		Positive, Negative
	}

	public enum Key {
		Key1, Key2
	}

}
