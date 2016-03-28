package ch.smaug.light.server.control.master.fsm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.smaug.light.server.control.master.fsm.LightStateInputEvent;
import ch.smaug.light.server.control.master.fsm.LightStateInputEventProvider;
import ch.smaug.light.server.control.master.fsm.LightStateInputEvent.InputEventType;
import ch.smaug.light.server.pi.KeyButtonEvent;
import ch.smaug.light.server.pi.KeyButtonEvent.Edge;
import ch.smaug.light.server.pi.KeyButtonEvent.Key;

@RunWith(CdiRunner.class)
public class LightStateInputEventProviderTest {

	@Inject
	private LightStateInputEventProvider testee;
	private LightStateInputEvent lastEvent;

	@Test
	public void processKeyButtonEvent_positiveEdge_firePositiveEdge() {
		// arrange
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Positive));
		// assert
		assertThat(lastEvent.getType(), is(equalTo(InputEventType.PositiveEdge)));
	}

	@Test
	public void processKeyButtonEvent_negativeEdge_fireNegativeEdge() {
		// arrange
		// act
		testee.processKeyButtonEvent(new KeyButtonEvent(Key.Key1, Edge.Negative));
		// assert
		assertThat(lastEvent.getType(), is(equalTo(InputEventType.NegativeEdge)));
	}

	public void process(@Observes final LightStateInputEvent event) {
		this.lastEvent = event;
	}
}
