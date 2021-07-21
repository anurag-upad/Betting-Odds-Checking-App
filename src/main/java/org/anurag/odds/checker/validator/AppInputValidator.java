package org.anurag.odds.checker.validator;

public class AppInputValidator {
	
	
	public Long validateAndGet(String[] input) {
		
		if(input.length != 1)
			throw new IllegalArgumentException("Please provide an Event Id as input");
		
		Long eventId;
		try {
			eventId = Long.parseLong(input[0]);
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException("Please provide a valid Event Id with Numeric characters only");
		}
		
		return eventId;
	}

}
