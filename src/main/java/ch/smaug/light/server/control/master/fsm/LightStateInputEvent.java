package ch.smaug.light.server.control.master.fsm;

public final class LightStateInputEvent {

	private final InputEventType type;

	public LightStateInputEvent(final InputEventType type) {
		this.type = type;
	}

	public InputEventType getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", getClass().getSimpleName(), type.name());
	}

	public enum InputEventType {
		NegativeEdge, PositiveEdge, Timeout
	}
}
