package ch.smaug.light.server.control.master;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.smaug.light.server.control.linearizing.LinearizedLightControl;

@RunWith(MockitoJUnitRunner.class)
public class MasterLightControlTest {

	@Mock
	private LinearizedLightControl lightControl;

	@InjectMocks
	private final MasterLightControl testee = new MasterLightControl();

	@Test
	public void turnOn_setLevelTo100Percent() {
		// Act
		testee.fullLight();
		// Assert
		verify(lightControl).setLevel(100);
	}

	@Test
	public void turnOn_setLevelTo0Percent() {
		// Act
		testee.turnOff();
		// Assert
		verify(lightControl).setLevel(0);
	}

	@Test
	public void dimDown_fullLight_decreaseLevel() {
		// Arrange
		testee.fullLight();
		// Act
		testee.dim();
		// Assert
		verify(lightControl).setLevel(99);
	}

	@Test
	public void dimDown_noLight_setLevelToMaximum() {
		// Arrange
		testee.turnOff();
		// Act
		testee.dim();
		testee.dim();
		// Assert
		verify(lightControl).setLevel(1);
	}
}
