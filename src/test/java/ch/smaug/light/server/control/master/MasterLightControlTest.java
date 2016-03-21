package ch.smaug.light.server.control.master;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.control.fading.FadingLightControl;

@RunWith(CdiRunner.class)
public class MasterLightControlTest {

	@Mock
	@Produces
	private FadingLightControl fadingLightControlMock;

	@Inject
	private MasterLightControl testee;

	@Test
	public void turnOff_setLevelToZero() {
		// Arrange
		// Act
		testee.turnOff();
		// Assert
		verify(fadingLightControlMock).setLevel(0);
	}

	@Test
	public void turnOn_setLevelToLastLevel() {
		// Arrange
		testee.setLevel(70);
		testee.turnOff();
		reset(fadingLightControlMock);
		// Act
		testee.turnOn();
		// Assert
		verify(fadingLightControlMock).setLevel(70);
	}

	@Test
	public void isOn_levelMoreThenZero_true() {
		// Arrange
		testee.setLevel(70);
		// Act
		final boolean on = testee.isOn();
		// Assert
		assertThat(on, is(true));
	}

	@Test
	public void isOn_levelMoreIsZero_false() {
		// Arrange
		testee.setLevel(0);
		// Act
		final boolean on = testee.isOn();
		// Assert
		assertThat(on, is(false));
	}

}
