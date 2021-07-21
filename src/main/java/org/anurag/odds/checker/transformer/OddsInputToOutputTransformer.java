package org.anurag.odds.checker.transformer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.anurag.odds.checker.constants.AppConstants;
import org.anurag.odds.checker.dto.BetOutcome;
import org.anurag.odds.checker.dto.ConsoleOutput;
import org.anurag.odds.checker.dto.LiveEvent;

/**
 * 
 * Mapper class for transforming input to output DTO
 *
 */
public class OddsInputToOutputTransformer {
	
	
	public static ConsoleOutput transformInputToOutput(LiveEvent filteredEvent) {
		
		ConsoleOutput consoleOutputObj = new ConsoleOutput();
		
		//set the Event name
		consoleOutputObj.setEventName(filteredEvent.getEvent().getName());
		
		//set the Event's label and its corresponding odds
		if(filteredEvent.getMainBetOffer() != null) {
			List<BetOutcome> eventOutcomes = filteredEvent.getMainBetOffer().getOutcomes();
			
			//1. for bet outcome with null odds, assuming odds to be of value 1
			//2. divide each of the odds value by 1000 and keep two decimal places
			Map<String,BigDecimal> resultOutcomes = eventOutcomes.stream()
															     .collect(Collectors.toMap(
															    		 	outcome -> outcome.getLabel(),
																			outcome -> outcome.getOdds() == null ? BigDecimal.ONE : 
																				outcome.getOdds().divide(AppConstants.DIVISION_FACTOR, 2, RoundingMode.HALF_EVEN)
															    		 ));
			
			consoleOutputObj.setLabelOddsMap(resultOutcomes);
		}
		
		return consoleOutputObj;
	}

}
