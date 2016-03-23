package ch.smaug.light.server.control.master;

import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;
import ch.smaug.light.server.control.master.LightStateEvent.Type;

@RunWith(CdiRunner.class)
public class MasterLightControlTest {

	@Mock
	@Produces
	private LinearizedLightControl lightControl;

	@Inject
	private MasterLightControl testee;

	@Test
	public void turnOn_setLevelTo100Percent() {
		// Act
		testee.consume(new LightStateEvent(Type.TurnOn));
		// Assert
		verify(lightControl).setLevel(100);
	}

	@Test
	public void turnOn_setLevelTo0Percent() {
		// Act
		testee.consume(new LightStateEvent(Type.TurnOff));
		// Assert
		verify(lightControl).setLevel(0);
	}
}
