package ch.smaug.light.server.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ch.smaug.light.server.control.fading.FadingLightControl;

@Path("control")
public class LightControlRestService {

	@Inject
	private FadingLightControl lightControl;

	@POST
	@Consumes("text/plain")
	public Response setLevel(final int value) {
		if (value < 0 || value >= lightControl.getNumberOfLevels()) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		lightControl.setLevel(value);
		return Response.ok().build();
	}
}
