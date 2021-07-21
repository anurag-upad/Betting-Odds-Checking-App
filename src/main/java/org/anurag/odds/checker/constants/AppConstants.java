package org.anurag.odds.checker.constants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Constants used across the OddsCheckingApp
 *
 */

public class AppConstants {
	
	public static final String KAMBI_REST_API_URL = "https://eu-offering.kambicdn.org/offering/v2018/ubse/event/live/open";
	
	public static final String EVENT_TYPE_MATCH = "MATCH";
	
	public static final BigDecimal DIVISION_FACTOR = new BigDecimal(1000);
	
	public static final String FOOTBALL = "FOOTBALL";
	
	public static final String FIRST_HALF = "1st half";	//excluding 2nd Half
	
	public static final String BASKETBALL = "BASKETBALL";
	
	public static final List<String> QUARTERS = Arrays.asList("1st Quarter","2nd Quarter","3rd Quarter"); //excluding 4th Quarter
	
	public static final String TENNIS = "TENNIS";
	
}
