package ch.smaug.light.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasspaths;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;
import ch.smaug.light.server.pi.RaspberryPiGatewayMock;

@RunWith(CdiRunner.class)
@AdditionalClasspaths(MasterLightControl.class)
@ActivatedAlternatives(RaspberryPiGatewayMock.class)
public class LightServerAcceptanceTest {

	@Inject
	private Event<KeyButtonEvent> keyButtonEvent;

	@Inject
	private RaspberryPiGatewayMock raspberryPiGatewayMock;

	@Test
	public void shortButtonPressing_lightIsOff_turnLightOn() {
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(1000));
	}

	@Test
	public void shortButtonPressing_lightIsOn_turnLightOff() {
		// arrange
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(1000));
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(0));
	}

	@Test
	public void longButtonPressing_lightIsOff_turnLightOnAndDecrease() {
		// arrange
		// act
		fireLongClick(1000);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is((greaterThan(0))));
		assertThat(pwmValue, is(lessThan(1000)));
	}

	private void fireShortClick() {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}

	private void fireLongClick(final long duration) {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}
}
