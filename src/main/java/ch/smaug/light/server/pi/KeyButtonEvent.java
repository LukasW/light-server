package ch.smaug.light.server.pi;

public class KeyButtonEvent {

	private final ClickType type;

	public enum ClickType {
		Short, Long
	}

	public KeyButtonEvent(final ClickType type) {
		this.type = type;
	}

	public ClickType getType() {
		return type;
	}
}
