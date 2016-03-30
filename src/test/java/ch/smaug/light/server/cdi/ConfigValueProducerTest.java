package ch.smaug.light.server.cdi;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class ConfigValueProducerTest {

	@Inject
	ConfigValueProducer testee;

	@Inject
	@ConfigValue(value = "foo", resource = "test-settings.xml")
	private Long foo;

	@Test
	public void config_longValue() {
		assertThat(foo, is(equalTo(12L)));
	}
}
