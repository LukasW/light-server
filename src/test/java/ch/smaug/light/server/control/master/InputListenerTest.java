package ch.smaug.light.server.control.master;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.ClickType;

@RunWith(CdiRunner.class)
public class InputListenerTest {

	@SuppressWarnings("unused") // Used by the cdi test
	@Inject
	private InputListener inputListener;

	@Inject
	private Event<KeyButtonEvent> keyButtonEvent;

	@Mock
	@Produces
	private MasterLightControl masterLightControl;

	@Before
	public void setupMasterLightControl() {
		doReturn(100).when(masterLightControl).getNumberOfLevels();
	}

	@Test
	public void fireShortClick_inStateOn_turnOff() {
		// Arrange
		doReturn(true).when(masterLightControl).isOn();
		// Act
		keyButtonEvent.fire(new KeyButtonEvent(ClickType.Short));
		// Assert
		verify(masterLightControl).turnOff();
	}

	@Test
	public void fireShortClick_inStateOff_turnOn() {
		// Arrange
		doReturn(false).when(masterLightControl).isOn();
		// Act
		keyButtonEvent.fire(new KeyButtonEvent(ClickType.Short));
		// Assert
		verify(masterLightControl).turnOn();
	}

	@Test
	public void fireLongClick_inStateOff_turnOn() {
		// Arrange
		doReturn(false).when(masterLightControl).isOn();
		// Act
		keyButtonEvent.fire(new KeyButtonEvent(ClickType.Long));
		// Assert
		verify(masterLightControl).turnOn();
	}

}
