package ch.smaug.light.server.pi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative
@ApplicationScoped
public class RaspberryPiGatewayMock implements RaspberryPiGateway {
	private final static Logger LOG = LoggerFactory.getLogger(RaspberryPiGatewayMock.class);
	private int value;

	@Override
	public void setPwm(final int value) {
		this.value = value;
		LOG.info("Set Pwm value to {}.", value);
	}

	public int getPwm() {
		return value;
	}
}
