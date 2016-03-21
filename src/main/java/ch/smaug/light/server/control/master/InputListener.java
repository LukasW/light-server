package ch.smaug.light.server.control.master;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ch.smaug.light.server.pi.KeyButtonEvent;

@ApplicationScoped
public class InputListener {

	@Inject
	private MasterLightControl masterLightControl;

	public void onKeyButtonEvent(@Observes final KeyButtonEvent keyButtonEvent) {
		switch (keyButtonEvent.getType()) {
		case Short:
			onShortKeyButtonEvent();
			break;
		case Long:
			onLongKeyButtonEvent();
			break;
		}
	}

	private void onShortKeyButtonEvent() {
		if (masterLightControl.isOn()) {
			masterLightControl.turnOff();
		} else {
			masterLightControl.turnOn();
		}
	}

	private void onLongKeyButtonEvent() {
		masterLightControl.turnOn();
	}
}
