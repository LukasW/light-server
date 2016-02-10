package ch.smaug.light.server.control;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

/**
 * Linearized pwm steps.
 */
@Named
public class Linearizer {

	private final static List<Integer> PWM_LEVELS = Arrays.asList(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5,
			5, 5, 6, 6, 7, 8, 8, 9, 9, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 23, 25, 27, 29, 31, 33, 35, 38, 41, 44, 47, 50, 54, 58,
			62, 67, 71, 77, 82, 88, 95, 101, 109, 117, 125, 134, 144, 154, 165, 177, 190, 203, 218, 234, 250, 268, 288, 308, 330, 354, 380,
			407, 436, 467, 501, 537, 575, 616, 660, 708, 758, 813, 871, 933, 1000);

	public Integer getNumberOfLevels() {
		return PWM_LEVELS.size();
	}

	public Integer getValue(final Integer level) {
		return PWM_LEVELS.get(level);
	}
}
