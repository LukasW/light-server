package ch.smaug.light.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("control")
public class LightRestService {

	private final static Logger LOG = LoggerFactory.getLogger(LightRestService.class);

	@GET
	@Produces("text/plain")
	public Long getValue() {
		return LightControl.getLightControl().getValue();
	}

	@POST
	@Consumes("text/plain")
	public void setValue(final long value) {
		LightControl.getLightControl().setValue(value);
	}
}
