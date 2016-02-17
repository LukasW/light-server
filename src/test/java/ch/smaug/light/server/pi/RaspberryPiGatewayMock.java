package ch.smaug.light.server.pi;

import javax.enterprise.inject.Alternative;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative
public class RaspberryPiGatewayMock implements RaspberryPiGateway {
	private final static Logger LOG = LoggerFactory.getLogger(RaspberryPiGatewayMock.class);

	@Override
	public void setPwm(final int value) {
		LOG.info("Set Pwm value to {}.", value);
	}
}
