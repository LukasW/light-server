package ch.smaug.light.server.daemon;

import javax.enterprise.inject.spi.CDI;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.smaug.light.server.pi.RaspberryPiGatewayImpl;
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
		CDI.current().select(RaspberryPiGatewayImpl.class).get().setPwm(0); // TODO Nicer implementation
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
		redirectJULtoSlf4j();

		final LightServerDaemon lightServerDaemon = new LightServerDaemon();
		lightServerDaemon.init(null);
		lightServerDaemon.start();
	}

	private static void redirectJULtoSlf4j() {
		// Optionally remove existing handlers attached to j.u.l root logger
		SLF4JBridgeHandler.removeHandlersForRootLogger(); // (since SLF4J 1.6.5)

		// add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
		// the initialization phase of your application
		SLF4JBridgeHandler.install();
	}
}
