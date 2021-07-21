package org.anurag.odds.checker.utils;

import java.math.BigDecimal;
import java.util.Map;

public class OddsCheckerUtility {
	
	
	public static boolean compareOddsForAnyChanges(Map<String,BigDecimal> previousOdds, Map<String,BigDecimal> newOdds) {
		
		boolean isOddsDifferent = false;
		if(newOdds != null) {
			for(Map.Entry<String, BigDecimal> entry : previousOdds.entrySet()) {

				String previousLabel = entry.getKey();
				BigDecimal previousOdd = entry.getValue();

				BigDecimal newOdd = newOdds.get(previousLabel);

				if(!previousOdd.equals(newOdd)) {
					isOddsDifferent = true;
					break;
				}
			}
		}
		
		return isOddsDifferent;
	}


}
