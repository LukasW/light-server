package ch.smaug.light.server.control.master.fsm.event;

public class LightStateInputEvent {

	private final Type type;

	protected LightStateInputEvent(final Type type) {
		this.type = type;
	}

	public static LightStateInputEvent createTimeoutEvent() {
		return new LightStateInputEvent(Type.Timeout);
	}

	public static LightStateInputKeyEvent createNegativeEdgeEvent(final String keyName) {
		return new LightStateInputKeyEvent(Type.NegativeEdge, keyName);
	}

	public static LightStateInputKeyEvent createPositiveEdgeEvent(final String keyName) {
		return new LightStateInputKeyEvent(Type.PositiveEdge, keyName);
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		NegativeEdge, PositiveEdge, Timeout
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LightStateInputEvent other = (LightStateInputEvent) obj;
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
