package ch.smaug.light.server.control.master.fsm;

public class LightStateOutputEvent {

	public enum Type {
		TurnOn, TurnOff, Previous
	}

	private final Type type;

	public LightStateOutputEvent(final Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "LightStateOutputEvent[" + type + "]";
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
		final LightStateOutputEvent other = (LightStateOutputEvent) obj;
		if (type != other.type) {
			return false;
		}
		return true;
	}

}
