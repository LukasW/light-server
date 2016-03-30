package ch.smaug.light.server.cdi;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class TestConfigValueProducer {

	private final Map<String, Object> values = new HashMap<>();

	@Produces
	@Alternative
	@ConfigValue("")
	public Long createLongConfigValue(final InjectionPoint injectionPoint) {
		final String key = injectionPoint.getAnnotated().getAnnotation(ConfigValue.class).value();
		if (!values.containsKey(key)) {
			throw new RuntimeException("Configuration value " + key + " is not set.");
		}
		return (Long) values.get(key);
	}

	public void setConfigValue(final String key, final Object value) {
		values.put(key, value);
	}
}
