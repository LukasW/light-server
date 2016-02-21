package ch.smaug.light.server.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class FadingLightControl implements LightControl {

	@Inject
	private LinearizedLightControl linearizedLightControl;

	private final FadingThread fadingThread = new FadingThread() {
		@Override
		protected void setActualLevel(final int actualLevel) {
			linearizedLightControl.setLevel(actualLevel);
		}
	};

	FadingLightControl() {
		super();
		fadingThread.start();
	}

	@Override
	public int getNumberOfLevels() {
		return linearizedLightControl.getNumberOfLevels();
	}

	@Override
	public void setLevel(final int level) {
		fadingThread.setExpectedLevel(level);
	}

}
