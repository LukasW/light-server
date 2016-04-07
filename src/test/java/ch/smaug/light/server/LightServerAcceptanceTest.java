package ch.smaug.light.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalClasspaths;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
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

	private final Semaphore timeout = new Semaphore(0);

	@Test
	public void lightIsOff_oneShortClick_turnsLightOn() {
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		waitForEndOfTimeout();
		// assert
		assertThat(pwmValue, is(1000));
	}

	@Test
	public void lightIsOff_oneLongClick_turnsLightOnAndDimsOneStepDown() {
		// arrange
		// act
		fireLongClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(933)));
	}

	@Test
	public void lightIsOff_oneTwiceLongClick_turnsLightOnAndDimsTwoStepsDown() {
		// arrange
		// act
		fireLongClick(2);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(871)));
	}

	@Test
	public void lightIsOn_oneShortClick_turnsLightOff() {
		// arrange
		turnOn();
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(0));
	}

	@Test
	public void lightIsOn_oneLongClick_dimsOneStepDown() {
		// arrange
		turnOn();
		// act
		fireLongClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(933)));
	}

	@Test
	public void lightIsOnAndDimed_turnOffAndOn_dimedStateIsRestored() {
		// arrange
		turnOn();
		fireLongClick();
		assertThat(raspberryPiGatewayMock.getPwm(), is(equalTo(933)));
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(equalTo(0)));
		// act
		fireShortClick();
		waitForEndOfTimeout();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(933)));
	}

	@Test
	public void lightIsOff_moreThenOneKeyIsPressed_firstKeyWins() {
		// arrange
		// act
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key2, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key2, Edge.Negative));
		waitForEndOfTimeout();
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
		// assert
		assertThat(raspberryPiGatewayMock.getPwm(), is(equalTo(933)));
	}

	private void turnOn() {
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(1000));
		waitForEndOfTimeout();
	}

	private void fireShortClick() {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}

	private void fireLongClick(final int timeouts) {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		for (int i = 0; i < timeouts; i++) {
			waitForEndOfTimeout();
		}
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}

	private void fireLongClick() {
		fireLongClick(1);
	}

	private void waitForEndOfTimeout() {
		try {
			final boolean acquire = timeout.tryAcquire(1, TimeUnit.SECONDS);
			assertThat(acquire, is(equalTo(true)));
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void onLightStateInputEvent(@Observes final LightStateInputEvent event) {
		if (event.equals(LightStateInputEvent.createTimeoutEvent())) {
			timeout.release();
		}
	}
}
