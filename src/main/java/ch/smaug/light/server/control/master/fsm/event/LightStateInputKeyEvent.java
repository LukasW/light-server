package ch.smaug.light.server.control.master.fsm.event;

public class LightStateInputKeyEvent extends LightStateInputEvent {

	private final String keyName;

	protected LightStateInputKeyEvent(final Type type, final String keyName) {
		super(type);
		this.keyName = keyName;
	}

	public String getKeyName() {
		return keyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LightStateInputKeyEvent other = (LightStateInputKeyEvent) obj;
		if (keyName == null) {
			if (other.keyName != null) {
				return false;
			}
		} else if (!keyName.equals(other.keyName)) {
			return false;
		}
		return true;
	}
}
