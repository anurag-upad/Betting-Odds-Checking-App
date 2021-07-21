package org.anurag.odds.checker.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 
 * POJO to handle console output in the asked/expected format 
 *
 */
public class ConsoleOutput {
	
	private String eventName;
	
	private Map<String,BigDecimal> labelOddsMap;
	
	private String oddsTimeStamp;
	
	
	//Getters and Setters start from here
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Map<String, BigDecimal> getLabelOddsMap() {
		return labelOddsMap;
	}

	public void setLabelOddsMap(Map<String, BigDecimal> labelOddsMap) {
		this.labelOddsMap = labelOddsMap;
	}
	
	public String getOddsTimeStamp() {
		return oddsTimeStamp;
	}

	public void setOddsTimeStamp(String oddsTimeStamp) {
		this.oddsTimeStamp = oddsTimeStamp;
	}
	

	@Override
	public String toString() {
		StringBuilder outputBuilder = new StringBuilder();

		outputBuilder.append("Event: ")
					 .append(this.eventName)
					 .append(System.lineSeparator());
		
		outputBuilder.append(displayOddsChanges());

		return outputBuilder.toString();
	}
	

	public String displayOddsChanges() {
		StringBuilder outputBuilder = new StringBuilder();

		outputBuilder.append("[").append(this.oddsTimeStamp)
					 .append("]").append(" | ");
		
		if(this.labelOddsMap != null) {
			for(Map.Entry<String, BigDecimal> entry : this.labelOddsMap.entrySet()) {
				
				outputBuilder.append(entry.getKey()).append(":   ")
				.append(entry.getValue()).append(" | ");
			}
		}
		
		return outputBuilder.toString();
	}
	
	
}
	
