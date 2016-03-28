package ch.smaug.light.server.control.master.fsm.state;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;

public interface State {

	State process(LightStateInputEvent event);
}
