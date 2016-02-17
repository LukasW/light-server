package ch.smaug.light.server.pi;

import javax.inject.Named;

@Named
public interface RaspberryPiGateway {

	void setPwm(final int value);
}
