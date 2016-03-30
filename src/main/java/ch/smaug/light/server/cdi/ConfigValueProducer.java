package ch.smaug.light.server.cdi;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ConfigValueProducer {

	private final static Logger LOG = LoggerFactory.getLogger(ConfigValueProducer.class);

	private final Map<String, Properties> propertiesByResource = new HashMap<>();

	@Produces
	@ConfigValue("")
	public Long createLongConfigValue(final InjectionPoint injectionPoint) {
		return Long.valueOf(getProperty(getConfigKey(injectionPoint)));
	}

	private String getProperty(final ConfigKey configKey) {
		Properties properties = propertiesByResource.get(configKey.getResource());
		if (properties == null) {
			properties = loadProperties(configKey.getResource());
			propertiesByResource.put(configKey.getResource(), properties);
		}
		return properties.getProperty(configKey.getKey());
	}

	private ConfigKey getConfigKey(final InjectionPoint injectionPoint) {
		final ConfigValue configValue = injectionPoint.getAnnotated().getAnnotation(ConfigValue.class);
		return new ConfigKey(configValue.value(), configValue.resource());
	}

	private Properties loadProperties(final String resourceName) {
		LOG.info("Loading properties from {}.", resourceName);
		final Properties properties = new Properties();
		try {
			try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
				properties.loadFromXML(inputStream);
			}
		} catch (final IOException e) {
			LOG.error("Can not load application-settings.xml", e);
		}
		return properties;
	}

	private final class ConfigKey {

		private final String key;
		private final String resource;

		public ConfigKey(final String key, final String resource) {
			this.key = key;
			this.resource = resource;
		}

		public String getKey() {
			return key;
		}

		public String getResource() {
			return resource;
		}
	}
}
