package org.anurag.odds.checker.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

import org.anurag.odds.checker.constants.AppConstants;
import org.anurag.odds.checker.constants.AppTestData;
import org.anurag.odds.checker.constants.HttpObjects;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveEvent;
import org.anurag.odds.checker.exception.OddsCheckerAppException;
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
import org.mockito.MockitoAnnotations;


@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class OddsInfoServiceImplTest {
	
	@Mock
	private HttpClient mockHttpClient;
	
	@Mock
	private HttpResponse<String> mockHttpResponse;
	
	@InjectMocks
	private OddsInfoServiceImpl underTest;

	private KambiApiResponse apiResponseTestData;
	
	@BeforeAll
	void beforeAll() {
		MockitoAnnotations.initMocks(this);
		apiResponseTestData = AppTestData.API_RESPONSE_TEST_DATA;
	}
	
	
	@Test
	@Order(1)
	@DisplayName("Kambi REST API response should be returned successfully as expected")
	void testGetOddsInfoByKambiApiReturnsResponse() throws Exception {
		
		when(mockHttpClient.send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString())).
		thenReturn(mockHttpResponse);
		
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn(AppTestData.TEST_JSON_STRING);
		
		KambiApiResponse apiResponse = underTest.getOddsInfoByKambiApiUrl(mockHttpClient, HttpObjects.HTTP_GET_REQUEST);
		
		assertTrue(!apiResponse.getLiveEvents().isEmpty());
		
		LiveEvent liveEvent = apiResponse.getLiveEvents().get(0);
		assertAll(
				() -> assertTrue(liveEvent.getEvent().getTags().contains(AppConstants.EVENT_TYPE_MATCH)),
				() -> assertTrue(liveEvent.getLiveData().getMatchClock().isRunning()),
				() -> assertEquals("TENNIS", liveEvent.getEvent().getSport()),
                () -> assertEquals("Roger Fedrer-Novak Djokovic", liveEvent.getEvent().getName())
        );
		
		verify(mockHttpClient, times(1)).send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString());
	}
	
	
	@Test
	@Order(2)
	@DisplayName("Exception should be thrown while de-serializing JSON in incorrect format/structure")
	void testGetOddsInfoByKambiApiThrowsException() throws IOException, InterruptedException {
		
		when(mockHttpClient.send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString())).
		thenReturn(mockHttpResponse);
	    
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn(AppTestData.TEST_JSON_STRING.substring(1));	//sending JSON in incorrect format

		OddsCheckerAppException exception = assertThrows(OddsCheckerAppException.class, 
				() -> underTest.getOddsInfoByKambiApiUrl(mockHttpClient, HttpObjects.HTTP_GET_REQUEST));
		
		assertEquals(OddsInfoServiceImpl.ERROR_MSG_JSON_DESERIALIZATION, exception.getMessage());
	}
	
	
	@Test
	@Order(3)
	@DisplayName("No Exception should be thrown while de-serializing JSON in correct format/structure")
	void testGetOddsInfoByKambiApiThrowsNoException() throws IOException, InterruptedException {
		
		when(mockHttpClient.send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString())).
		thenReturn(mockHttpResponse);
	    
		when(mockHttpResponse.statusCode()).thenReturn(200);
		when(mockHttpResponse.body()).thenReturn(AppTestData.TEST_JSON_STRING);

		assertDoesNotThrow( () -> underTest.getOddsInfoByKambiApiUrl(mockHttpClient, HttpObjects.HTTP_GET_REQUEST));
	}
	
	
	@Test
	@Order(4)
	@DisplayName("No Exception should be thrown while de-serializing JSON after adding a fresh new element in it")
	void testGetOddsInfoByKambiApiThrowsNoException2() throws IOException, InterruptedException {
		
		when(mockHttpClient.send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString())).
		thenReturn(mockHttpResponse);
	    
		when(mockHttpResponse.statusCode()).thenReturn(200);
		
		//added a new property in "event", say "isExciting", to indicate if odds are very frequently changing in an event or not
		//but our service shouldn't break
		when(mockHttpResponse.body()).thenReturn(AppTestData.TEST_JSON_NEW_ELEMENT_ADDED);
		
		assertDoesNotThrow( () -> underTest.getOddsInfoByKambiApiUrl(mockHttpClient, HttpObjects.HTTP_GET_REQUEST));
	}
	
	
	@Test
	@Order(5)
	@DisplayName("No Exception should be thrown while de-serializing JSON after removing an existing element from it")
	void testGetOddsInfoByKambiApiThrowsNoException3() throws IOException, InterruptedException {
		
		when(mockHttpClient.send(HttpObjects.HTTP_GET_REQUEST, HttpResponse.BodyHandlers.ofString())).
		thenReturn(mockHttpResponse);
	    
		when(mockHttpResponse.statusCode()).thenReturn(200);
		
		//removed an existing property in "event", say "tags"; but our service shouldn't break
		when(mockHttpResponse.body()).thenReturn(AppTestData.TEST_JSON_OLD_ELEMENT_REMOVED);
		
		assertDoesNotThrow( () -> underTest.getOddsInfoByKambiApiUrl(mockHttpClient, HttpObjects.HTTP_GET_REQUEST));
	}
	
	
	@Test
	@Order(6)
	@DisplayName("Live Event data returned should be as expected")
	void testFilterResponseByEventIdandTag() throws OddsCheckerAppException {
		
		LiveEvent filteredEvent = underTest.filterResponseByEventIdandTag(apiResponseTestData, 2000L);
		
		assertAll(
				//assert event data
				() -> assertEquals(2000L, filteredEvent.getEvent().getId()),
				() -> assertTrue(filteredEvent.getEvent().getTags().contains(AppConstants.EVENT_TYPE_MATCH)),
				() -> assertEquals("Real Madrid - Barcelona FC", filteredEvent.getEvent().getName()),
				() -> assertEquals("FOOTBALL", filteredEvent.getEvent().getSport()),
				
				//assert live time data
				() -> assertEquals(2000L, filteredEvent.getLiveData().getEventId()),
				() -> assertTrue(filteredEvent.getLiveData().getMatchClock().isRunning()),
				() -> assertEquals(AppConstants.FIRST_HALF, filteredEvent.getLiveData().getMatchClock().getPeriod()),
				
				//assert odds data
				() -> assertEquals(2000L, filteredEvent.getMainBetOffer().getEventId()),
				() -> assertTrue(!filteredEvent.getMainBetOffer().getOutcomes().isEmpty()),
				() -> assertEquals("Real Madrid", filteredEvent.getMainBetOffer().getOutcomes().get(0).getLabel()),
				() -> assertEquals(new BigDecimal(5250), filteredEvent.getMainBetOffer().getOutcomes().get(0).getOdds())
        );
	}
	
	@Test
	@Order(7)
	@DisplayName("Live Event data should not be returned for Event ID which doesn't exist")
	void testFilterResponseByEventIdandTag2() throws OddsCheckerAppException {
		
		//for event id - 12345, we don't have corresponding event data in our Api response test data
		OddsCheckerAppException exception = assertThrows(OddsCheckerAppException.class, 
				() -> underTest.filterResponseByEventIdandTag(apiResponseTestData, 12345L));
		
		assertTrue(exception.getMessage().startsWith("No matching event found for the input Event Id"));
		
	}
	
	@Test
	@Order(8)
	@DisplayName("Live Event data should not be returned for TAG value - COMPETITION, though Event ID exists")
	void testFilterResponseByEventIdandTag3() throws OddsCheckerAppException {
		
		//for event id - 5000 in our test data, TAG value is COMPETITION
		OddsCheckerAppException exception = assertThrows(OddsCheckerAppException.class, 
				() -> underTest.filterResponseByEventIdandTag(apiResponseTestData, 5000L));
		
		assertTrue(exception.getMessage().equals("No matching event found for the input Event Id : 5000 and Tag as MATCH in JSON response."));
		
	}
	
	@Test
	@Order(9)
	@DisplayName("For a finished match, polling API interval should become 0, indicating no need of polling API further for any odds changes")
	void testGetPollIntervalByMatchStatus() {
		
		//this match is finished in test data : running = false, disabled = true
		LiveEvent finishedEventData = apiResponseTestData.getLiveEvents().get(4); 	
		
		AtomicInteger pollingInterval = new AtomicInteger(10);
		
		underTest.getPollIntervalByMatchStatus(finishedEventData, pollingInterval);
		int newPollingInterval = pollingInterval.get();
		
		assertEquals(0, newPollingInterval);
		
		assertEquals("India - England", finishedEventData.getEvent().getName());
		assertEquals("CRICKET", finishedEventData.getEvent().getSport());
		
		assertFalse(finishedEventData.getLiveData().getMatchClock().isRunning());
		assertTrue(finishedEventData.getLiveData().getMatchClock().isDisabled());
	}
	
	@Test
	@Order(10)
	@DisplayName("For a running match, polling API interval shouldn't change")
	void testGetPollIntervalByMatchStatus2() {
		
		//this match is running in test data (running = true)
		LiveEvent liveEventData = apiResponseTestData.getLiveEvents().get(0); 	
		
		AtomicInteger pollingInterval = new AtomicInteger(10);
		
		int oldPollingInterval = pollingInterval.get();
		underTest.getPollIntervalByMatchStatus(liveEventData, pollingInterval);
		int newPollingInterval = pollingInterval.get();
		
		assertEquals(10, newPollingInterval);
		assertEquals(oldPollingInterval, newPollingInterval);
		
		assertEquals("Roger Fedrer-Novak Djokovic", liveEventData.getEvent().getName());
		assertEquals("TENNIS", liveEventData.getEvent().getSport());
		
		assertTrue(liveEventData.getLiveData().getMatchClock().isRunning());
		assertFalse(liveEventData.getLiveData().getMatchClock().isDisabled());
		
	}
	
	
	@Test
	@Order(11)
	@DisplayName("For a running Football match gone into Half time, polling API interval should be increased (say, to 300 s) as odds seldom change during Half times")
	void testGetPollIntervalByMatchStatus3() {
		
		//this Football match is in running state in test data, but currently in Half-time
		LiveEvent liveEventData = apiResponseTestData.getLiveEvents().get(6); 	
		
		AtomicInteger pollingInterval = new AtomicInteger(10);
		
		int oldPollingInterval = pollingInterval.get();
		underTest.getPollIntervalByMatchStatus(liveEventData, pollingInterval);
		int newPollingInterval = pollingInterval.get();
		
		assertEquals(300, newPollingInterval);
		assertNotEquals(oldPollingInterval, newPollingInterval);
		
		assertEquals("Manchaster United - Chelsea", liveEventData.getEvent().getName());
		assertEquals("FOOTBALL", liveEventData.getEvent().getSport());
		
		assertTrue(liveEventData.getLiveData().getMatchClock().isRunning());
		assertFalse(liveEventData.getLiveData().getMatchClock().isDisabled());
		assertEquals(AppConstants.FIRST_HALF, liveEventData.getLiveData().getMatchClock().getPeriod());
		assertEquals(0, liveEventData.getLiveData().getMatchClock().getMinutesLeftInPeriod());
		assertEquals(0, liveEventData.getLiveData().getMatchClock().getSecondsLeftInMinute());
		
	}
	
	@Test
	@Order(12)
	@DisplayName("For a running Basketball match gone into Quarter time, polling API interval should be increased (say, to 120 s) as odds seldom change during Quarter time")
	void testGetPollIntervalByMatchStatus4() {
		
		//this Basketball match is in running state in test data, but currently in Quarter-time
		LiveEvent liveEventData = apiResponseTestData.getLiveEvents().get(2); 	
		
		AtomicInteger pollingInterval = new AtomicInteger(10);
		
		int oldPollingInterval = pollingInterval.get();
		underTest.getPollIntervalByMatchStatus(liveEventData, pollingInterval);
		int newPollingInterval = pollingInterval.get();
		
		assertEquals(120, newPollingInterval);
		assertNotEquals(oldPollingInterval, newPollingInterval);
		
		assertEquals("New York Knicks - Chicago Bulls", liveEventData.getEvent().getName());
		assertEquals("BASKETBALL", liveEventData.getEvent().getSport());
		
		assertTrue(liveEventData.getLiveData().getMatchClock().isRunning());
		assertFalse(liveEventData.getLiveData().getMatchClock().isDisabled());
		assertTrue(AppConstants.QUARTERS.contains(liveEventData.getLiveData().getMatchClock().getPeriod()));
		assertEquals(0, liveEventData.getLiveData().getMatchClock().getMinutesLeftInPeriod());
		assertEquals(0, liveEventData.getLiveData().getMatchClock().getSecondsLeftInMinute());
		
	}
	
}
