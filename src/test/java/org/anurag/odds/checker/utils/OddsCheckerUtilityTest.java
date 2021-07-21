package org.anurag.odds.checker.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class OddsCheckerUtilityTest {
	
	
	@Test
	@DisplayName("For same values of label and their corresponding odds means Odds didn't change")
	void testCompareOddsForAnyChanges() {
		
		Map<String,BigDecimal> previousOddsMap = new HashMap<>();
		previousOddsMap.put("a", BigDecimal.ONE);
		previousOddsMap.put("b", BigDecimal.TEN);
		
		Map<String,BigDecimal> currentOddsMap = new HashMap<>();
		currentOddsMap.put("b", BigDecimal.TEN);
		currentOddsMap.put("a", BigDecimal.ONE);
		
		
		boolean result = OddsCheckerUtility.compareOddsForAnyChanges(previousOddsMap, currentOddsMap);
		//odds didn't change
		assertFalse(result);
	}
	
	@Test
	@DisplayName("For different values of label and their corresponding odds means Odds did change")
	void testCompareOddsForAnyChanges2() {
		
		Map<String,BigDecimal> previousOddsMap = new HashMap<>();
		previousOddsMap.put("a", BigDecimal.ONE);
		previousOddsMap.put("b", BigDecimal.TEN);
		
		Map<String,BigDecimal> currentOddsMap = new HashMap<>();
		currentOddsMap.put("b", BigDecimal.ONE);
		currentOddsMap.put("a", BigDecimal.TEN);
		
		
		boolean result = OddsCheckerUtility.compareOddsForAnyChanges(previousOddsMap, currentOddsMap);
		//odds did change
		assertTrue(result);
	}
	
	@Test
	@DisplayName("For a label/key missing in the new odds would result in Odds change")
	void testCompareOddsForAnyChanges3() {
		
		Map<String,BigDecimal> previousOddsMap = new HashMap<>();
		previousOddsMap.put("a", BigDecimal.ONE);
		previousOddsMap.put("b", BigDecimal.TEN);
		previousOddsMap.put("c", new BigDecimal(12.5));
		
		Map<String,BigDecimal> currentOddsMap = new HashMap<>();
		currentOddsMap.put("b", BigDecimal.ONE);
		currentOddsMap.put("a", BigDecimal.TEN);
		
		
		boolean result = OddsCheckerUtility.compareOddsForAnyChanges(previousOddsMap, currentOddsMap);
		//odds did change
		assertTrue(result);
	}

}
