package ch.smaug.light.server.control.master.fsm.machine;

import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jglue.cdiunit.ActivatedAlternatives;
import org.jglue.cdiunit.AdditionalPackages;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.smaug.light.server.cdi.DeferredEvent;
import ch.smaug.light.server.cdi.TestConfigValueProducer;
import ch.smaug.light.server.control.master.MasterLightControl;
import ch.smaug.light.server.control.master.fsm.event.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.state.OffState;
import ch.smaug.light.server.control.master.fsm.state.OnState;

@RunWith(CdiRunner.class)
@AdditionalPackages(OffState.class)
@ActivatedAlternatives(TestConfigValueProducer.class)
public class LightStateEventMachineIntegrationTest {

	@Inject
	private TestConfigValueProducer testConfigValueProducer;
	@Inject
	private LightStateEventMachine testee;

	@Inject
	private Instance<Object> instance;

	@Mock
	@Produces
	private DeferredEvent<LightStateInputEvent> delayLightStateInputEventSender;

	@Mock
	@Produces
	private MasterLightControl masterLightControl;

	@Before
	public void setDefaultConfigValues() {
		testConfigValueProducer.setConfigValue("startingTimeout", 0L);
		testConfigValueProducer.setConfigValue("repeatingTimeout", 0L);
	}

	@Test
	public void turnLightOn() {
		// Arrange
		// Act
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		// Assert
		verify(masterLightControl).turnOn();
	}

	@Test
	public void turnLightOff() {
		// Arrange
		testee.setState(instance.select(OnState.class).get());
		// Act
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createTimeoutEvent());
		// Assert
		verify(masterLightControl).turnOff();
	}

	@Test
	public void turnLightOff_dim() {
		// Arrange
		testee.setState(instance.select(OnState.class).get());
		// Act
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createTimeoutEvent());
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		// Assert
		verify(masterLightControl).dim();
	}

	@Test
	public void turnLightOn_dim() {
		// Arrange
		// Act
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createTimeoutEvent());
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		// Assert
		final InOrder inOrder = Mockito.inOrder(masterLightControl);
		inOrder.verify(masterLightControl).turnOn();
		inOrder.verify(masterLightControl).dim();
		inOrder.verify(masterLightControl).isOff();
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void turnLightOn_fullLight() {
		// Arrange
		// Act
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createPositiveEdgeEvent("K1"));
		testee.processEvent(LightStateInputEvent.createNegativeEdgeEvent("K1"));
		// Assert
		final InOrder inOrder = Mockito.inOrder(masterLightControl);
		inOrder.verify(masterLightControl).turnOn();
		inOrder.verify(masterLightControl).fullLight();
		inOrder.verifyNoMoreInteractions();
	}
}
