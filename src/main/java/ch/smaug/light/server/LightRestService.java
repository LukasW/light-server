package ch.smaug.light.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("control")
public class LightRestService {

	@GET
	@Produces("text/plain")
	public int getValue() {
		return LightControl.getLightControl().getValue();
	}

	@POST
	@Consumes("text/plain")
	public void setValue(final int value) {
		LightControl.getLightControl().setValue(value);
	}
}
