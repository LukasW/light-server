package ch.smaug.light.server.daemon;

import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.LightControl;
import ch.smaug.light.server.pi.RaspberryPiGatewayMock;

@RunWith(CdiRunner.class)
@ActivatedAlternatives(RaspberryPiGatewayMock.class)
public class DaemonTest {

	@Inject
	private LightControl lightControl;

	@Test
	public void foo() throws InterruptedException {
		lightControl.fade();
	}
}
