package org.anurag.odds.checker.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.anurag.odds.checker.constants.AppConstants;
import org.anurag.odds.checker.dto.ConsoleOutput;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveEvent;
import org.anurag.odds.checker.dto.MatchClock;
import org.anurag.odds.checker.exception.OddsCheckerAppException;
import org.anurag.odds.checker.utils.ConversionUtils;
import org.anurag.odds.checker.utils.OddsCheckerUtility;

import com.fasterxml.jackson.core.JsonProcessingException;

public class OddsInfoServiceImpl implements OddsInfoService {
	
	public static final String ERROR_MSG_HTTP = "HTTP GET request for Kambi REST API cannot be completed.";
	
	public static final String ERROR_MSG_JSON_DESERIALIZATION = "JSON string response from REST API couldn't be de-serialized.";

	public static final String ERROR_MSG_EVENT_ID_NOT_FOUND = "No matching event found for the input Event Id : %d and Tag as MATCH in JSON response.";
	
	public static final String ERROR_MSG_GENERIC = "Some unexpected error occurred. Please try again after some time.";
	
	
	@Override
	public KambiApiResponse getOddsInfoByKambiApiUrl(HttpClient httpClient, HttpRequest httpRequest) throws OddsCheckerAppException {

		KambiApiResponse kambiApiResponse = null;
		try {

			HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			
			//if successful, HTTP GET request will return 200 code 
			if(response.statusCode() == 200)	
				kambiApiResponse = ConversionUtils.covertJsonStringToObject(response.body(), KambiApiResponse.class);
				
		}
		catch(JsonProcessingException ex) {
			throw new OddsCheckerAppException(ERROR_MSG_JSON_DESERIALIZATION, ex);
		}	
		catch (IOException | InterruptedException ex) {
			throw new OddsCheckerAppException(ERROR_MSG_HTTP, ex);
		}
		catch(Exception ex) {
			throw new OddsCheckerAppException(ERROR_MSG_GENERIC, ex);
		}
		
		return kambiApiResponse;
	}
	
	
	@Override
	public LiveEvent filterResponseByEventIdandTag(KambiApiResponse apiResponse, Long eventId) throws OddsCheckerAppException {
		
		LiveEvent filteredEvent;
		try{
			filteredEvent = apiResponse.getLiveEvents().stream()
													   .filter(e -> e.getEvent().getId().equals(eventId)
																		&& e.getEvent().getTags().contains(AppConstants.EVENT_TYPE_MATCH))
													   .findFirst()
													   .orElseThrow();
			
		} catch(NoSuchElementException ex) {
			throw new OddsCheckerAppException(String.format(ERROR_MSG_EVENT_ID_NOT_FOUND, eventId), ex);
		}
		
		return filteredEvent;
	}

	
	@Override
	public String compareAndDisplayOddsChanges(ConsoleOutput consoleOutputObj, Map<String,BigDecimal> previousOdds) {
		
		String consoleOutputString = "";
		
		boolean isOddsDifferent = true;
		if(previousOdds.isEmpty()) {

			//for the first time, print Event name along with odds; toString() will be called implicitly
			consoleOutputString = consoleOutputObj.toString();
		}
		else {

			//compare new odds with previous odds for any changes
			Map<String,BigDecimal> newOdds = consoleOutputObj.getLabelOddsMap();
			isOddsDifferent = OddsCheckerUtility.compareOddsForAnyChanges(previousOdds, newOdds);

			if(isOddsDifferent) {
				//for the subsequent calls, display only odds changes on the console
				consoleOutputString = consoleOutputObj.displayOddsChanges();
			}
		}

		//store current odds for subsequent comparison with upcoming odds
		if(consoleOutputObj.getLabelOddsMap() != null) {
			previousOdds.putAll(consoleOutputObj.getLabelOddsMap());
		}
		
		return consoleOutputString;
	}
	

	@Override
	public void getPollIntervalByMatchStatus(LiveEvent filteredEvent, AtomicInteger pollingInterval) {

		MatchClock clockStatus= filteredEvent.getLiveData().getMatchClock();

		//case 1 : match is running/live
		if(clockStatus.isRunning()) {

			//For now, implementing the "dynamic poll time interval" logic for these sports - Football, Basketball
			//For FOOTBALL matches - during 1st half breaks, increase poll interval 
			if(AppConstants.FOOTBALL.equals(filteredEvent.getEvent().getSport()) && isHalfTimeBreak(filteredEvent)) {

				//setting poll interval to 5 minutes as odds rarely change during half times
				pollingInterval.set(300);
			}

			//For BASKETBALL matches - during 1st Quarter, 2nd Quarter, 3rd Quarter breaks, increase poll interval
			else if(AppConstants.BASKETBALL.equals(filteredEvent.getEvent().getSport()) && isHalfTimeBreak(filteredEvent)) {

				//setting poll interval to 2 minutes as odds rarely change during half times
				pollingInterval.set(120);
			}
			else {
				//don't change polling interval, rather reset it to original interval of 10 seconds
				pollingInterval.set(10);
			}

		}
		else {

			//case 2 : match is not running i.e. has finished (running=false, disabled=true)
			if(clockStatus.isDisabled()) {

				//Match has finished. No need to poll Kambi API further for odds changes.
				pollingInterval.set(0);
			}
			else {
				//case 3 : might be a time-out
				pollingInterval.set(10);
			}
		}
	}
	
	private boolean isHalfTimeBreak(LiveEvent filteredEvent) {

		boolean isHalfTimeBreak = false;
		if((AppConstants.FIRST_HALF.equals(filteredEvent.getLiveData().getMatchClock().getPeriod()) ||
				AppConstants.QUARTERS.contains(filteredEvent.getLiveData().getMatchClock().getPeriod())) && 
				filteredEvent.getLiveData().getMatchClock().getMinutesLeftInPeriod() == 0 &&
				filteredEvent.getLiveData().getMatchClock().getSecondsLeftInMinute() == 0) {
			
			isHalfTimeBreak = true;
		}
		
		return isHalfTimeBreak;
	}


}
