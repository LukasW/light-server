package ch.smaug.light.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightControl {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	private long value;
	private final static LightControl INSTANCE = new LightControl();

	public static LightControl getLightControl() {
		return INSTANCE;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		LOG.info("Set value from {} to {}.", this.value, value);
		this.value = value;
	}
}
