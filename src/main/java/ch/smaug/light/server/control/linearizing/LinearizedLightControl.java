package ch.smaug.light.server.control.linearizing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ch.smaug.light.server.control.common.LightControl;
import ch.smaug.light.server.pi.RaspberryPiGateway;

@Named
@ApplicationScoped
public class LinearizedLightControl implements LightControl {

	@Inject
	private RaspberryPiGateway raspberryPiGateway;

	@Inject
	private Linearizer linearizer;

	LinearizedLightControl() {
		super();
	}

	@Override
	public int getNumberOfLevels() {
		return linearizer.getNumberOfLevels();
	}

	@Override
	public void setLevel(final int level) {
		if (level < 0 || level >= getNumberOfLevels()) {
			throw new IllegalArgumentException(String.format("value must be in in [0..%d[", getNumberOfLevels()));
		}
		raspberryPiGateway.setPwm(linearizer.getValue(level));
	}
}
