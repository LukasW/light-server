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

	private static final int NO_LIGHT = 0;

	private static final int TWO_STEPS_DOWN = 871;

	private static final int ONE_STEP_DOWN = 933;

	private static final int FULL_LIGHT = 1000;

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
		assertThat(pwmValue, is(FULL_LIGHT));
	}

	@Test
	public void lightIsOff_oneLongClick_turnsLightOnAndDimsOneStepDown() {
		// arrange
		// act
		fireLongClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(ONE_STEP_DOWN)));
	}

	@Test
	public void lightIsOff_oneTwiceLongClick_turnsLightOnAndDimsTwoStepsDown() {
		// arrange
		// act
		fireLongClick(2);
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(TWO_STEPS_DOWN)));
	}

	@Test
	public void lightIsOn_oneShortClick_turnsLightOff() {
		// arrange
		turnOn();
		// act
		fireShortClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(NO_LIGHT));
	}

	@Test
	public void lightIsOn_oneLongClick_dimsOneStepDown() {
		// arrange
		turnOn();
		// act
		fireLongClick();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(ONE_STEP_DOWN)));
	}

	@Test
	public void lightIsOnAndDimed_turnOffAndOn_dimedStateIsRestored() {
		// arrange
		turnOn();
		fireLongClick();
		assertThat(raspberryPiGatewayMock.getPwm(), is(equalTo(ONE_STEP_DOWN)));
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(equalTo(NO_LIGHT)));
		// act
		fireShortClick();
		waitForEndOfTimeout();
		final int pwmValue = raspberryPiGatewayMock.getPwm();
		// assert
		assertThat(pwmValue, is(equalTo(ONE_STEP_DOWN)));
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
		assertThat(raspberryPiGatewayMock.getPwm(), is(equalTo(ONE_STEP_DOWN)));
	}

	private void turnOn() {
		fireShortClick();
		assumeThat(raspberryPiGatewayMock.getPwm(), is(FULL_LIGHT));
		waitForEndOfTimeout();
	}

	private void fireShortClick() {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Negative));
	}

	private void fireLongClick(final int timeouts) {
		keyButtonEvent.fire(new KeyButtonEvent(Key.Key1, Edge.Positive));
		for (int i = NO_LIGHT; i < timeouts; i++) {
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
