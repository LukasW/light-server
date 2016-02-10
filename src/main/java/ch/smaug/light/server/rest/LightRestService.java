package ch.smaug.light.server.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ch.smaug.light.server.control.LightControl;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("control")
public class LightRestService {

	@Inject
	private LightControl lightControl;

	@GET
	@Produces("text/plain")
	public int getValue() {
		return lightControl.getValue();
	}

	@GET
	@Path("fade")
	@Produces("text/plain")
	public void fade() {
		lightControl.fade();
	}

	@POST
	@Consumes("text/plain")
	public void setValue(final int value) {
		lightControl.setValue(value);
	}
}
