package ch.smaug.light.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasspaths;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;
import ch.smaug.light.server.pi.RaspberryPiGatewayMock;

@RunWith(CdiRunner.class)
@AdditionalClasspaths(MasterLightControl.class)
@ActivatedAlternatives(RaspberryPiGatewayMock.class)
public class LightServerAcceptanceTest {

	private static final Logger LOG = LoggerFactory.getLogger(LightServerAcceptanceTest.class);
	@Inject
	private Event<KeyButtonEvent> keyButtonEvent;

	@Inject
	private RaspberryPiGatewayMock raspberryPiGatewayMock;

	@Test
	public void turnLightOn() {
		LOG.info("*** Start test turnLightOn");
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(1000));
		LOG.info("*** End test turnLightOn");
	}

	@Test
	public void turnLightOff() {
		LOG.info("*** Start test turnLightOff");
		// arrange
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(1000));
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(0));
		LOG.info("*** End test turnLightOff");
	}

	@Test
	public void turnLightOnAndOneStepDimDown() {
		LOG.info("*** Start test turnLightOnAndOneStepDimDown");
		// arrange
		// act
		fireLongClick(75);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(933)));
		LOG.info("*** *** End test turnLightOnAndOneStepDimDown");
	}

	@Test
	public void turnLightOnAndTwoStepsDimDown() {
		LOG.info("*** Start test turnLightOnAndTwoStepsDimDown");
		// arrange
		// act
		fireLongClick(125);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(871)));
		LOG.info("*** End test turnLightOnAndTwoStepsDimDown");
	}

	@Test
	public void dimOneStepUp() {
		LOG.info("*** Start test dimOneStepUp");
		// arrange
		fireLongClick(125);
		// act
		fireLongClick(75);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(933)));
		LOG.info("*** End test dimOneStepUp");
	}

	private void fireShortClick() {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}

	private void fireLongClick(final long duration) {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		try {
			Thread.sleep(duration);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}
}
