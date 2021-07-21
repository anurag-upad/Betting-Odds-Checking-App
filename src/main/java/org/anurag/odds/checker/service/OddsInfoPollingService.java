package org.anurag.odds.checker.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.anurag.odds.checker.constants.HttpObjects;
import org.anurag.odds.checker.dto.ConsoleOutput;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveEvent;
import org.anurag.odds.checker.transformer.OddsInputToOutputTransformer;
import org.anurag.odds.checker.utils.ConversionUtils;

public class OddsInfoPollingService {

	private static final ScheduledExecutorService schedular = Executors.newScheduledThreadPool(1);
	
	private final AtomicInteger pollingInterval;
	
	private final Long inputEventId;
	
	private ScheduledFuture<?> schedulerFuture;
	
	private final Map<String,BigDecimal> previousOdds;
	
	private final Consumer<String> callback;
	
	private final OddsInfoService oddsInfoService;
	

	public OddsInfoPollingService(Long inputEventId, Consumer<String> callback, AtomicInteger pollingInterval, OddsInfoService oddsInfoService) {
		this.inputEventId = inputEventId;
		this.previousOdds = new HashMap<>();
		this.callback = callback;
		this.pollingInterval = pollingInterval;
		this.oddsInfoService = oddsInfoService;
	}
	
	
	public void pollKambiApiForOddsChanges() {

		//create a thread pool that will execute task periodically after each polling interval
		this.schedulerFuture = schedular.scheduleAtFixedRate(this::execute, 0, pollingInterval.get(), TimeUnit.SECONDS);
	}
	

	public void execute() {
		
		StringBuilder consoleOutputString = new StringBuilder();
		try {
			String startTime = ConversionUtils.getDateWithoutNanoSec(LocalDateTime.now());
			
			//fetch Odds info from public Kambi API 
			KambiApiResponse apiResponse = oddsInfoService.getOddsInfoByKambiApiUrl(HttpObjects.HTTP_CLIENT, HttpObjects.HTTP_GET_REQUEST);

			if(apiResponse == null) {
				consoleOutputString.append("No response was returned by Kambi API, will try again after 10 seconds");
				return;
			}

			//filter out above API response for Event with given Event Id and Tag as "MATCH" 
			LiveEvent filteredEvent = oddsInfoService.filterResponseByEventIdandTag(apiResponse, inputEventId);
			
			//transform input to output for display to console
			ConsoleOutput consoleOutputObj = OddsInputToOutputTransformer.transformInputToOutput(filteredEvent);
			consoleOutputObj.setOddsTimeStamp(startTime);
			
			//compare previous odds with current odds and display if changed
			consoleOutputString.append(oddsInfoService.compareAndDisplayOddsChanges(consoleOutputObj, previousOdds));
			
			//fetch polling interval based on event's current status and if changed, update
			getCompareAndUpdatePollInterval(filteredEvent, oddsInfoService);
			
			//prints the output on the console using a callback
			if(consoleOutputString.length() != 0)
				callback.accept(consoleOutputString.toString());
		}
		catch(Exception e) {
			
			//print the returned error message on the console
			System.out.println(e.getMessage());
			
			//release resources gracefully
			this.releaseResources();
			
			System.exit(1);
		}
		
	}
	

	private void getCompareAndUpdatePollInterval(LiveEvent filteredEvent, OddsInfoService oddsInfoService) {
		
		int oldPollingInterval = pollingInterval.get();
		
		//get updated polling time interval based on whether match is running or not
		if(filteredEvent.getLiveData() != null && filteredEvent.getLiveData().getMatchClock() != null) {
			
			oddsInfoService.getPollIntervalByMatchStatus(filteredEvent, pollingInterval);
		}
		
		int newPollingInterval = pollingInterval.get();
		
		//update polling time interval if there is a change
		if(oldPollingInterval != newPollingInterval) {
			updatePollingInterval(newPollingInterval);
		}
	}
	
	
	private void updatePollingInterval(int newPollingInterval) {
		
		this.releaseResources();

		if(newPollingInterval != 0) {
			//update polling interval to the new value
			pollingInterval.set(newPollingInterval);

			//create a new scheduler with the updated polling interval
			this.schedulerFuture = schedular.scheduleAtFixedRate(this::execute, pollingInterval.get(), pollingInterval.get(), TimeUnit.SECONDS);
		}
		else {
			//match has ended, so no need to poll Kambi API further
		}
	}


	private void releaseResources() {
		
		if (schedulerFuture != null) {
            schedulerFuture.cancel(true);
        }
	}

}
