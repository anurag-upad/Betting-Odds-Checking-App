package org.anurag.odds.checker.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * Need to have a complete list of Sport names to use this Enum.
 * For now, won't be using this Enum
 * since during de-serialization of JSON, if a sport name comes up
 * which is not there in this Enum's declarations, it would throw an exception
 *
 */
public enum Sport {
	
	FOOTBALL(Arrays.asList("1st half","2nd half"), 15), 
	BASKETBALL(Arrays.asList("1st Quarter","2nd Quarter","3rd Quarter", "4th Quarter"), 2),
	ICE_HOCKEY(Arrays.asList("1st period","2nd period","3rd period"), 15),
	CRICKET(Arrays.asList("1st half","2nd half"), 30),
	TENNIS(Arrays.asList(""), 2),
	GOLF(Arrays.asList(""), 1);
	
	
	private List<String> breakTerms;
	private int breakTimeMinutes;
	
	private Sport(List<String> breakTerms, int breakTimeMinutes) {
		this.breakTerms = breakTerms;
		this.breakTimeMinutes = breakTimeMinutes;
	}

	public List<String> getBreakTerms() {
		return breakTerms;
	}

	public void setBreakTerms(List<String> breakTerms) {
		this.breakTerms = breakTerms;
	}

	public int getBreakTimeMinutes() {
		return breakTimeMinutes;
	}

	public void setBreakTimeMinutes(int breakTimeMinutes) {
		this.breakTimeMinutes = breakTimeMinutes;
	}
	
}
