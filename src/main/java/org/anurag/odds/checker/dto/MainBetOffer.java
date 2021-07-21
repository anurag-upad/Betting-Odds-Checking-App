package org.anurag.odds.checker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainBetOffer {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("eventId")
	private Long eventId;
	
	@JsonProperty("outcomes")
	private List<BetOutcome> outcomes;

	
	//constructors start from here
	public MainBetOffer() {}
	
	public MainBetOffer(Long id, Long eventId, List<BetOutcome> outcomes) {
		this.id = id;
		this.eventId = eventId;
		this.outcomes = outcomes;
	}


	//Getters and Setters start from here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public List<BetOutcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<BetOutcome> outcomes) {
		this.outcomes = outcomes;
	}
	
	
	@Override
	public String toString() {
		return "MainBetOffer [id=" + id + ", eventId=" + eventId + ", outcomes=" + outcomes + "]";
	}
}
