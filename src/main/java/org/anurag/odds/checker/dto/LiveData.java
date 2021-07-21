package org.anurag.odds.checker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveData {
	
	@JsonProperty("eventId")
	private Long eventId;
	
	@JsonProperty("matchClock")
	private MatchClock matchClock;
	

	//constructors start from here
	public LiveData() {}
	
	public LiveData(Long eventId, MatchClock matchClock) {
		this.eventId = eventId;
		this.matchClock = matchClock;
	}
	
	
	//Getters and Setters start from here
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public MatchClock getMatchClock() {
		return matchClock;
	}

	public void setMatchClock(MatchClock matchClock) {
		this.matchClock = matchClock;
	}
	

	@Override
	public String toString() {
		return "LiveData [eventId=" + eventId + ", matchClock=" + matchClock + "]";
	}
	
}
