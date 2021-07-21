package org.anurag.odds.checker.constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.anurag.odds.checker.dto.BetOutcome;
import org.anurag.odds.checker.dto.EventData;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveData;
import org.anurag.odds.checker.dto.LiveEvent;
import org.anurag.odds.checker.dto.MainBetOffer;
import org.anurag.odds.checker.dto.MatchClock;

public class AppTestData {

	
	public static final String TEST_JSON_STRING = 
			"{\"liveEvents\":[{\"event\":{\"id\":1000,\"name\":\"Roger Fedrer-Novak Djokovic\",\"sport\":\"TENNIS\",\"tags\":[\"MATCH\"]},"
			+ "\"liveData\":{\"eventId\":1000,\"matchClock\":{\"minute\":257,\"second\":39,\"running\":true}},"
			+ "\"mainBetOffer\":{\"id\":1001,\"eventId\":1000,\"outcomes\":"
			+ "[{\"id\":1002,\"label\":\"Roger Fedrer\",\"odds\":1400},{\"id\":1003,\"label\":\"Novak Djokovic\",\"odds\":1200}]}},"
			+ "{\"event\":{\"id\":2000,\"name\":\"Real Madrid - Barcelona FC\",\"sport\":\"FOOTBALL\",\"tags\":[\"MATCH\",\"OPEN_FOR_LIVE\"]},"
			+ "\"liveData\":{\"eventId\":2000,\"matchClock\":{\"running\":false}},"
			+ "\"mainBetOffer\":{\"id\":2001,\"eventId\":2000,\"outcomes\":[{\"id\":2002,\"label\":\"Real Madrid\",\"odds\":5250},"
			+ "{\"id\":2003,\"label\":\"Draw\",\"odds\":3100},{\"id\":2004,\"label\":\"Barcelona FC\",\"odds\":1740}]}}]}";
	
	
	//added a new property in "event", say "isExciting", to indicate if odds are very frequently changing in an event
	public static final String TEST_JSON_NEW_ELEMENT_ADDED = 
			"{\"liveEvents\":[{\"event\":{\"id\":1000, \"isExciting\": true, \"name\":\"Roger Fedrer-Novak Djokovic\",\"sport\":\"TENNIS\",\"tags\":[\"MATCH\"]},"
			+ "\"liveData\":{\"eventId\":1000,\"matchClock\":{\"minute\":257,\"second\":39,\"running\":true}},"
			+ "\"mainBetOffer\":{\"id\":1001,\"eventId\":1000,\"outcomes\":"
			+ "[{\"id\":1002,\"label\":\"Roger Fedrer\",\"odds\":1400},{\"id\":1003,\"label\":\"Novak Djokovic\",\"odds\":1200}]}},"
			+ "{\"event\":{\"id\":2000,\"name\":\"Real Madrid - Barcelona FC\",\"sport\":\"FOOTBALL\",\"tags\":[\"MATCH\",\"OPEN_FOR_LIVE\"]},"
			+ "\"liveData\":{\"eventId\":2000,\"matchClock\":{\"running\":false}},"
			+ "\"mainBetOffer\":{\"id\":2001,\"eventId\":2000,\"outcomes\":[{\"id\":2002,\"label\":\"Real Madrid\",\"odds\":5250},"
			+ "{\"id\":2003,\"label\":\"Draw\",\"odds\":3100},{\"id\":2004,\"label\":\"Barcelona FC\",\"odds\":1740}]}}]}";
	
	
	//removed an existing property in "event", say "sport"
	public static final String TEST_JSON_OLD_ELEMENT_REMOVED = 
			"{\"liveEvents\":[{\"event\":{\"id\":1000,\"name\":\"Roger Fedrer-Novak Djokovic\",\"tags\":[\"MATCH\"]},"
					+ "\"liveData\":{\"eventId\":1000,\"matchClock\":{\"minute\":257,\"second\":39,\"running\":true}},"
					+ "\"mainBetOffer\":{\"id\":1001,\"eventId\":1000,\"outcomes\":"
					+ "[{\"id\":1002,\"label\":\"Roger Fedrer\",\"odds\":1400},{\"id\":1003,\"label\":\"Novak Djokovic\",\"odds\":1200}]}},"
					+ "{\"event\":{\"id\":2000,\"name\":\"Real Madrid - Barcelona FC\",\"sport\":\"FOOTBALL\",\"tags\":[\"MATCH\",\"OPEN_FOR_LIVE\"]},"
					+ "\"liveData\":{\"eventId\":2000,\"matchClock\":{\"running\":false}},"
					+ "\"mainBetOffer\":{\"id\":2001,\"eventId\":2000,\"outcomes\":[{\"id\":2002,\"label\":\"Real Madrid\",\"odds\":5250},"
					+ "{\"id\":2003,\"label\":\"Draw\",\"odds\":3100},{\"id\":2004,\"label\":\"Barcelona FC\",\"odds\":1740}]}}]}";
	
	
	public static final KambiApiResponse API_RESPONSE_TEST_DATA = getApiResponseTestData();
	
	public static final KambiApiResponse API_RESPONSE_TEST_DATA_ODDS_CHANGE = getApiResponseTestData2();


	private static KambiApiResponse getApiResponseTestData() {
		
		final KambiApiResponse apiResponseTestData = new KambiApiResponse();
		
		List<LiveEvent> liveEvents = new ArrayList<>();
		apiResponseTestData.setLiveEvents(liveEvents);
		
		EventData eventData1 = new EventData(1000L, "Roger Fedrer-Novak Djokovic", "TENNIS", Arrays.asList("MATCH"));
		LiveData liveData1 = new LiveData(1000L, new MatchClock(true, false, "3rd set", 30, 0));
		MainBetOffer mainBetOffer1 = new MainBetOffer(1001L, 1000L, Arrays.asList(new BetOutcome(1002L, "Roger Fedrer", new BigDecimal(1400)),
																				  new BetOutcome(1003L, "Novak Djokovic", new BigDecimal(1200))));
		
		
		EventData eventData2 = new EventData(2000L, "Real Madrid - Barcelona FC", "FOOTBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData2 = new LiveData(2000L, new MatchClock(true, false, "1st half", 30, 0));
		MainBetOffer mainBetOffer2 = new MainBetOffer(2001L, 2000L, Arrays.asList(new BetOutcome(2002L, "Real Madrid", new BigDecimal(5250)),
																				  new BetOutcome(2003L, "Draw", new BigDecimal(3100)), 
																				  new BetOutcome(2004L, "Barcelona FC", new BigDecimal(1740))));
		
		EventData eventData3 = new EventData(3000L, "New York Knicks - Chicago Bulls", "BASKETBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData3 = new LiveData(3000L, new MatchClock(true, false, "2nd Quarter", 0, 0));
		MainBetOffer mainBetOffer3 = new MainBetOffer(3001L, 3000L, Arrays.asList(new BetOutcome(3002L, "New York Knicks", new BigDecimal(7500)),
																				  new BetOutcome(3003L, "Chicago Bulls", new BigDecimal(1500))));
		
		EventData eventData4 = new EventData(4000L, "Norway - Sweden", "ICE_HOCKEY", Arrays.asList("MATCH", "PREMATCH_STATS"));
		LiveData liveData4 = new LiveData(4000L, new MatchClock(true, false, "3rd period", 12,27));
		MainBetOffer mainBetOffer4 = new MainBetOffer(4001L, 4000L, Arrays.asList(new BetOutcome(4002L, "Norway", new BigDecimal(65000)),
																				  new BetOutcome(4003L, "Draw", new BigDecimal(2900)),
																				  new BetOutcome(4004L, "Sweden", new BigDecimal(1000))));
		
		EventData eventData5 = new EventData(5000L, "India - England", "CRICKET", Arrays.asList("COMPETITION"));
		LiveData liveData5 = new LiveData(5000L, new MatchClock(false, true, "2nd half", 0, 0));
		MainBetOffer mainBetOffer5 = new MainBetOffer(5001L, 5000L, Arrays.asList(new BetOutcome(5002L, "India", new BigDecimal(1100)),
																				  new BetOutcome(5003L, "England", new BigDecimal(1400))));
		
		EventData eventData6 = new EventData(6000L, "Italy - France", "RUGBY_UNION", Arrays.asList("MATCH"));
		LiveData liveData6 = new LiveData(6000L, new MatchClock(false, false, "2nd half", 8, 21));
		MainBetOffer mainBetOffer6 = new MainBetOffer(6001L, 6000L, Arrays.asList(new BetOutcome(6002L, "Italy", new BigDecimal(2400)),
																				  new BetOutcome(6003L, "France", new BigDecimal(1800))));
		
		EventData eventData7 = new EventData(7000L, "Manchaster United - Chelsea", "FOOTBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData7 = new LiveData(7000L, new MatchClock(true, false, "1st half", 0, 0));
		MainBetOffer mainBetOffer7 = new MainBetOffer(7001L, 7000L, Arrays.asList(new BetOutcome(7002L, "Manchaster United", new BigDecimal(3200)),
																				  new BetOutcome(7004L, "Chelsea", new BigDecimal(2750))));
				
		liveEvents.add(new LiveEvent(eventData1, liveData1, mainBetOffer1));
		liveEvents.add(new LiveEvent(eventData2, liveData2, mainBetOffer2));
		liveEvents.add(new LiveEvent(eventData3, liveData3, mainBetOffer3));
		liveEvents.add(new LiveEvent(eventData4, liveData4, mainBetOffer4));
		liveEvents.add(new LiveEvent(eventData5, liveData5, mainBetOffer5));
		liveEvents.add(new LiveEvent(eventData6, liveData6, mainBetOffer6));
		liveEvents.add(new LiveEvent(eventData7, liveData7, mainBetOffer7));
		
		return apiResponseTestData;
	}
	
	
	private static KambiApiResponse getApiResponseTestData2() {
		
		final KambiApiResponse apiResponseTestData = new KambiApiResponse();
		
		List<LiveEvent> liveEvents = new ArrayList<>();
		apiResponseTestData.setLiveEvents(liveEvents);
		
		EventData eventData1 = new EventData(1000L, "Roger Fedrer-Novak Djokovic", "TENNIS", Arrays.asList("MATCH"));
		LiveData liveData1 = new LiveData(1000L, new MatchClock(true, false, "3rd set", 30, 0));
		MainBetOffer mainBetOffer1 = new MainBetOffer(1001L, 1000L, Arrays.asList(new BetOutcome(1002L, "Roger Fedrer", new BigDecimal(1600)),
																				  new BetOutcome(1003L, "Novak Djokovic", new BigDecimal(1100))));
		
		
		EventData eventData2 = new EventData(2000L, "Real Madrid - Barcelona FC", "FOOTBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData2 = new LiveData(2000L, new MatchClock(true, false, "1st half", 0, 0));
		MainBetOffer mainBetOffer2 = new MainBetOffer(2001L, 2000L, Arrays.asList(new BetOutcome(2002L, "Real Madrid", new BigDecimal(5000)),
																				  new BetOutcome(2003L, "Draw", new BigDecimal(3500)), 
																				  new BetOutcome(2004L, "Barcelona FC", new BigDecimal(1800))));
		
		EventData eventData3 = new EventData(3000L, "New York Knicks - Chicago Bulls", "BASKETBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData3 = new LiveData(3000L, new MatchClock(true, false, "3rd Quarter", 10, 0));
		MainBetOffer mainBetOffer3 = new MainBetOffer(3001L, 3000L, Arrays.asList(new BetOutcome(3002L, "New York Knicks", new BigDecimal(9000)),
																				  new BetOutcome(3003L, "Chicago Bulls", new BigDecimal(1200))));
		
		EventData eventData4 = new EventData(4000L, "Norway - Sweden", "ICE_HOCKEY", Arrays.asList("MATCH", "PREMATCH_STATS"));
		LiveData liveData4 = new LiveData(4000L, new MatchClock(false, true, "4th period", 0, 0));
		MainBetOffer mainBetOffer4 = new MainBetOffer(4001L, 4000L, Arrays.asList(new BetOutcome(4002L, "Norway", new BigDecimal(55000)),
																				  new BetOutcome(4003L, "Draw", new BigDecimal(2900)),
																				  new BetOutcome(4004L, "Sweden", new BigDecimal(1100))));
		
		EventData eventData5 = new EventData(5000L, "India - England", "CRICKET", Arrays.asList("COMPETITION"));
		LiveData liveData5 = new LiveData(5000L, new MatchClock(false, true, "2nd half", 0, 0));
		MainBetOffer mainBetOffer5 = new MainBetOffer(5001L, 5000L, Arrays.asList(new BetOutcome(5002L, "India", new BigDecimal(1000)),
																				  new BetOutcome(5003L, "England", new BigDecimal(10000))));
		
		EventData eventData6 = new EventData(6000L, "Italy - France", "RUGBY_UNION", Arrays.asList("MATCH"));
		LiveData liveData6 = new LiveData(6000L, new MatchClock(false, false, "2nd half", 8, 21));
		MainBetOffer mainBetOffer6 = new MainBetOffer(6001L, 6000L, Arrays.asList(new BetOutcome(6002L, "Italy", new BigDecimal(1800)),
																				  new BetOutcome(6003L, "France", new BigDecimal(2100))));
		
		EventData eventData7 = new EventData(7000L, "Manchaster United - Chelsea", "FOOTBALL", Arrays.asList("MATCH", "OPEN_FOR_LIVE"));
		LiveData liveData7 = new LiveData(7000L, new MatchClock(true, false, "2nd half", 45, 0));
		MainBetOffer mainBetOffer7 = new MainBetOffer(7001L, 7000L, Arrays.asList(new BetOutcome(7002L, "Manchaster United", new BigDecimal(3700)),
																				  new BetOutcome(7004L, "Chelsea", new BigDecimal(2550))));
				
		liveEvents.add(new LiveEvent(eventData1, liveData1, mainBetOffer1));
		liveEvents.add(new LiveEvent(eventData2, liveData2, mainBetOffer2));
		liveEvents.add(new LiveEvent(eventData3, liveData3, mainBetOffer3));
		liveEvents.add(new LiveEvent(eventData4, liveData4, mainBetOffer4));
		liveEvents.add(new LiveEvent(eventData5, liveData5, mainBetOffer5));
		liveEvents.add(new LiveEvent(eventData6, liveData6, mainBetOffer6));
		liveEvents.add(new LiveEvent(eventData7, liveData7, mainBetOffer7));
		
		return apiResponseTestData;
	}

}
