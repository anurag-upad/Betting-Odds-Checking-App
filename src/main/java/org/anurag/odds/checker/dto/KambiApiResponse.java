package org.anurag.odds.checker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KambiApiResponse {
	
	@JsonProperty("liveEvents")
	private List<LiveEvent> liveEvents;
	
	
	//constructors start from here
	public KambiApiResponse() {}
	
	public KambiApiResponse(List<LiveEvent> liveEvents) {
		this.liveEvents = liveEvents;
	}

	
	//Getters and Setters start from here
	public List<LiveEvent> getLiveEvents() {
		return liveEvents;
	}

	public void setLiveEvents(List<LiveEvent> liveEvents) {
		this.liveEvents = liveEvents;
	}


	@Override
	public String toString() {
		return "KambiApiResponse [liveEvents=" + liveEvents + "]";
	}

}
