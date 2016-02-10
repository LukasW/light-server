package ch.smaug.light.server.daemon;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.smaug.light.server.rest.RestServer;

public class LightServerDaemon implements Daemon {

	private final static Logger LOG = LoggerFactory.getLogger(LightServerDaemon.class);

	private final RestServer restServer = new RestServer();
	private Weld weld;

	@Override
	public void init(final DaemonContext daemonContext) {
		initialzeCdi();
	}

	@Override
	public void start() {
		LOG.info("Starting daemon");
		restServer.start();
	}

	@Override
	public void stop() {
		restServer.stop();
		LOG.info("Stopping daemon");
	}

	@Override
	public void destroy() {
		shutdownCdi();
	}

	private final void initialzeCdi() {
		weld = new Weld();
		weld.initialize();
	}

	private void shutdownCdi() {
		weld.shutdown();
	}

	public static void main(final String[] args) {
		final LightServerDaemon lightServerDaemon = new LightServerDaemon();
		lightServerDaemon.init(null);
		lightServerDaemon.start();
	}
}
