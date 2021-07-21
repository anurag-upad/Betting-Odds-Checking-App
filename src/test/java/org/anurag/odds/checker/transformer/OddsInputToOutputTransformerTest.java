package org.anurag.odds.checker.transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Map;

import org.anurag.odds.checker.constants.AppTestData;
import org.anurag.odds.checker.dto.ConsoleOutput;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class OddsInputToOutputTransformerTest {
	
	private KambiApiResponse apiResponseTestData;
	
	
	@BeforeAll
	void beforeAll() {
		apiResponseTestData = AppTestData.API_RESPONSE_TEST_DATA;
	}
	
	@Test
	@DisplayName("For a input of type LiveEvent, a DTO of type ConsoleOutput should be returned")
	void testTransformInputToOutput() {
		
		LiveEvent filteredEvent = apiResponseTestData.getLiveEvents().get(0);
			
		ConsoleOutput consoleOutputObj = OddsInputToOutputTransformer.transformInputToOutput(filteredEvent);
		
		assertEquals("Roger Fedrer-Novak Djokovic", consoleOutputObj.getEventName());
		
		Map<String, BigDecimal> labelOddsMap = consoleOutputObj.getLabelOddsMap();
		assertEquals(new BigDecimal("1.40"), labelOddsMap.get("Roger Fedrer"));
		assertEquals(new BigDecimal("1.20"), labelOddsMap.get("Novak Djokovic"));
		assertEquals(2, labelOddsMap.get("Roger Fedrer").scale());
		assertEquals(2, labelOddsMap.get("Novak Djokovic").scale());
	}

}
