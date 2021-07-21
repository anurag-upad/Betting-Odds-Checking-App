package org.anurag.odds.checker;

import java.util.concurrent.atomic.AtomicInteger;

import org.anurag.odds.checker.service.OddsInfoPollingService;
import org.anurag.odds.checker.service.OddsInfoServiceImpl;
import org.anurag.odds.checker.validator.AppInputValidator;

public class OddsCheckerAppMain {

	public static void main(String[] input) {

		//validate input and fetch Event ID
		Long inputEventId = null;
		try{
			AppInputValidator validator = new AppInputValidator();
			inputEventId = validator.validateAndGet(input);
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		
		//continuously poll Kambi API with a default interval of 10 seconds
		//also, dynamically change the polling interval time based on Match status - running/finished/time-out/half-time
		OddsInfoPollingService pollingService = new OddsInfoPollingService(inputEventId, System.out::println, 
																			new AtomicInteger(10), new OddsInfoServiceImpl());
		pollingService.pollKambiApiForOddsChanges();
		
	}

}
