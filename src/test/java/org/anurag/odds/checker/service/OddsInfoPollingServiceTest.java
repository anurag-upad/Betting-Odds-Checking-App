package org.anurag.odds.checker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.anurag.odds.checker.constants.AppTestData;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.exception.OddsCheckerAppException;
import org.anurag.odds.checker.transformer.OddsInputToOutputTransformer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class OddsInfoPollingServiceTest {
	
	@InjectMocks
	private OddsInfoPollingService underTest;
	
    @Spy
	private OddsInfoService oddsInfoService= new OddsInfoServiceImpl();
	
	@Mock
	private OddsInputToOutputTransformer inputToOutputTransformer;

	//default polling time interval is 10 seconds
	private static final AtomicInteger pollingInterval = new AtomicInteger(10);
	
	private KambiApiResponse apiResponseTestData;
	
	private KambiApiResponse apiResponseTestData2;

	@BeforeAll
	void init() {
		MockitoAnnotations.initMocks(this);
		apiResponseTestData = AppTestData.API_RESPONSE_TEST_DATA;
		apiResponseTestData2 = AppTestData.API_RESPONSE_TEST_DATA_ODDS_CHANGE;
	}

	
	@Test
	@Order(1)
    @DisplayName("First call to server - match is running")
	void testExecute() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 1000L;
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput1(outputStr), pollingInterval, oddsInfoService);
        underTest.execute();
        
        //there should be no change in default polling time interval of 10 seconds
        assertEquals(10, pollingInterval.get());
	}
	
	private void verifyOutput1(String outputString){

		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);
		
		assertTrue(outputString.contains("Roger Fedrer-Novak Djokovic"));
		assertEquals("1.40", labelOddsMap.get("Roger Fedrer"));
		assertEquals("1.20", labelOddsMap.get("Novak Djokovic"));
	}
	
	@Test
	@Order(2)
    @DisplayName("Multiple calls to server - match is running with No change in odds")
	void testExecute2() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 1000L;
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput1(outputStr), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        underTest.execute();
        underTest.execute();
        
        //there should be no change in polling time interval
        assertEquals(10, pollingInterval.get());
	}
	
	@Test
	@Order(3)
    @DisplayName("Multiple calls to server - match is running with change in odds")
	void testExecute3() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 1000L;
		AtomicInteger counter = new AtomicInteger(1);
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput3(outputStr, counter), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        underTest.execute();
        underTest.execute();
        
        //there should be no change in polling time interval
        assertEquals(10, pollingInterval.get());
	}
	
	private void verifyOutput3(String outputString, AtomicInteger counter){
		
		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);
		
		if(counter.get() == 1) //even name prints for the first time only
			assertTrue(outputString.contains("Roger Fedrer-Novak Djokovic"));
		
		if(counter.get() % 2 != 0) {
			assertEquals("1.20", labelOddsMap.get("Novak Djokovic"));
			assertEquals("1.40", labelOddsMap.get("Roger Fedrer"));
		}else {
			assertEquals("1.10", labelOddsMap.get("Novak Djokovic"));
			assertEquals("1.60", labelOddsMap.get("Roger Fedrer"));
		}
		counter.incrementAndGet();
	}
	
	@Test
	@Order(4)
    @DisplayName("Multiple calls to server - match is running initially but finished later; change in odds")
	void testExecute4() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 4000L;
		AtomicInteger counter = new AtomicInteger(1);
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput4(outputStr, counter), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        underTest.execute();
        underTest.execute();
        
        //since match has finished, no need of polling Kambi API any further; polling time interval should be set to 0 
        assertEquals(0, pollingInterval.get());
	}
	
	private void verifyOutput4(String outputString, AtomicInteger counter){

		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);

		if(counter.get() == 1) {
			assertTrue(outputString.contains("Norway - Sweden")); //even name prints for the first time only
			assertEquals("65.00", labelOddsMap.get("Norway"));
			assertEquals("1.00", labelOddsMap.get("Sweden"));
		}else {
			assertEquals("55.00", labelOddsMap.get("Norway"));
			assertEquals("1.10", labelOddsMap.get("Sweden"));
		}
		counter.incrementAndGet();

	}
	
	@Test
	@Order(5)
    @DisplayName("Multiple calls to server - a Football match was running initially, but went into Half-time")
	void testExecute5() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 2000L;
		AtomicInteger counter = new AtomicInteger(1);
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput5(outputStr, counter), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        
        //For a running Football match going into Half-time, polling API interval should be increased as odds seldom change during Half-time
        assertEquals(300, pollingInterval.get());
	}
	
	private void verifyOutput5(String outputString, AtomicInteger counter){

		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);

		if(counter.get() == 1) {
			assertTrue(outputString.contains("Real Madrid - Barcelona FC")); //even name prints for the first time only
			assertEquals("5.25", labelOddsMap.get("Real Madrid"));
			assertEquals("1.74", labelOddsMap.get("Barcelona FC"));
		}else {
			assertEquals("5.00", labelOddsMap.get("Real Madrid"));
			assertEquals("1.80", labelOddsMap.get("Barcelona FC"));
		}
		counter.incrementAndGet();

	}
	
	@Test
	@Order(6)
    @DisplayName("Multiple calls to server - a Football match was into Half-time initially, but resumed later")
	void testExecute6() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 7000L;
		AtomicInteger counter = new AtomicInteger(1);
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput6(outputStr, counter), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        
        //For a Football match resuming after Half-time, polling time interval should be reset to default of 10 seconds
        assertEquals(10, pollingInterval.get());
	}
	
	private void verifyOutput6(String outputString, AtomicInteger counter){

		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);

		if(counter.get() == 1) {
			assertTrue(outputString.contains("Manchaster United - Chelsea")); //even name prints for the first time only
			assertEquals("3.20", labelOddsMap.get("Manchaster United"));
			assertEquals("2.75", labelOddsMap.get("Chelsea"));
		}else {
			assertEquals("3.70", labelOddsMap.get("Manchaster United"));
			assertEquals("2.55", labelOddsMap.get("Chelsea"));
		}
		counter.incrementAndGet();
	}
	
	@Test
	@Order(7)
    @DisplayName("Multiple calls to server - a Basketball match into Quarter-time initially, but resumed later")
	void testExecute7() throws OddsCheckerAppException {
		
		doReturn(apiResponseTestData).
		doReturn(apiResponseTestData2).
		when(oddsInfoService).getOddsInfoByKambiApiUrl(Mockito.any(), Mockito.any());
		
		Long eventId = 3000L;
		AtomicInteger counter = new AtomicInteger(1);
        underTest = new OddsInfoPollingService(eventId, outputStr -> verifyOutput7(outputStr, counter), pollingInterval, oddsInfoService);
        
        underTest.execute();
        underTest.execute();
        
        //For a Basketball match resuming after Quarter-time, polling API interval should be reset to default of 10 seconds
        assertEquals(10, pollingInterval.get());
	}
	
	private void verifyOutput7(String outputString, AtomicInteger counter){

		Map<String,String> labelOddsMap = extractOddsFromOutput(outputString);

		if(counter.get() == 1) {
			assertTrue(outputString.contains("New York Knicks - Chicago Bulls")); //even name prints for the first time only
			assertEquals("7.50", labelOddsMap.get("New York Knicks"));
			assertEquals("1.50", labelOddsMap.get("Chicago Bulls"));
		}else {
			assertEquals("9.00", labelOddsMap.get("New York Knicks"));
			assertEquals("1.20", labelOddsMap.get("Chicago Bulls"));
		}
		counter.incrementAndGet();

	}

	private Map<String,String> extractOddsFromOutput(String outputString){

		String[] subStrings=outputString.trim().split("\\|");

		//break the output string to extract label names and their odds
		Map<String,String> labelOddsMap = new HashMap<>();
		for(int i=1; i < subStrings.length; i++) {
			String subString=subStrings[i];
			String[] nameWithOdds=subString.split(":");

			labelOddsMap.put(nameWithOdds[0].trim(), nameWithOdds[1].trim());
		}
		
		return labelOddsMap;
	}

}
