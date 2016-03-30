package ch.smaug.light.server.control.master;

import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateOutputEvent;

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
		testee.consume(LightStateOutputEvent.TurnOn);
		// Assert
		verify(lightControl).setLevel(100);
	}

	@Test
	public void turnOn_setLevelTo0Percent() {
		// Act
		testee.consume(LightStateOutputEvent.TurnOff);
		// Assert
		verify(lightControl).setLevel(0);
	}

	@Test
	public void dimDown_fullLight_decreaseLevel() {
		// Arrange
		testee.consume(LightStateOutputEvent.TurnOn);
		// Act
		testee.consume(LightStateOutputEvent.DimDown);
		// Assert
		verify(lightControl).setLevel(99);
	}

	@Test
	public void dimDown_noLight_setLevelToMaximum() {
		// Arrange
		// Act
		testee.consume(LightStateOutputEvent.DimDown);
		// Assert
		verify(lightControl).setLevel(100);
	}

	@Test
	public void dimUp_noLight_increaseLevel() {
		// Arrange
		// Act
		testee.consume(LightStateOutputEvent.DimUp);
		// Assert
		verify(lightControl).setLevel(1);
	}

	@Test
	public void dimUp_fullLight_setLevelToMaximum() {
		// Arrange
		testee.consume(LightStateOutputEvent.TurnOn);
		// Act
		testee.consume(LightStateOutputEvent.DimUp);
		// Assert
		verify(lightControl).setLevel(0);
	}
}
