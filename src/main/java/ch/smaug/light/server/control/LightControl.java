package ch.smaug.light.server.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.pi.RaspberryPiGateway;
import ch.smaug.light.server.rest.LightRestService;

@Named
@ApplicationScoped
public class LightControl {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	private int value;

	@Inject
	private RaspberryPiGateway raspberryPiGateway;

	@Inject
	private Linearizer linearizer;

	LightControl() {
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		if (value < 0 || value > 1000) {
			throw new IllegalArgumentException("Only value between 0 and 1000 allowed.");
		}
		LOG.info("Set value from {} to {}.", this.value, value);
		this.value = value;
		raspberryPiGateway.setPwm(value);
	}

	public void fade() {
		new Thread() {

			@Override
			public void run() {

				try {
					for (int j = 0; j < 10; j++) {
						for (int i = 0; i < linearizer.getNumberOfLevels(); i++) {
							raspberryPiGateway.setPwm(linearizer.getValue(i));
							Thread.sleep(30);
						}
						for (int i = linearizer.getNumberOfLevels() - 2; i > 0; i--) {
							raspberryPiGateway.setPwm(linearizer.getValue(i));
							Thread.sleep(30);
						}
					}
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
