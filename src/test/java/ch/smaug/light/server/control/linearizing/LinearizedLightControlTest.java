package ch.smaug.light.server.control.linearizing;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.pi.RaspberryPiGateway;

@RunWith(CdiRunner.class)
public class LinearizedLightControlTest {

	@Mock
	@Produces
	private RaspberryPiGateway raspberryPiGateway;

	@Mock
	@Produces
	private Linearizer linearizer;

	@Inject
	private LinearizedLightControl testee;

	@Test
	public void setLevel_validLevel_setOnRaspberry() {
		doReturn(100).when(linearizer).getNumberOfLevels();
		doReturn(14).when(linearizer).getValue(12);
		testee.setLevel(12);
		verify(raspberryPiGateway).setPwm(14);
	}

}
