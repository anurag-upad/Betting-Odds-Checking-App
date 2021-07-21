package org.anurag.odds.checker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveEvent {
	
	@JsonProperty("event")
	private EventData event;
	
	@JsonProperty("liveData")
	private LiveData liveData;
	
	@JsonProperty("mainBetOffer")
	private MainBetOffer mainBetOffer;
	
	
	//constructors start from here
	public LiveEvent() {}
	
	public LiveEvent(EventData event, LiveData liveData, MainBetOffer mainBetOffer) {
		this.event = event;
		this.liveData = liveData;
		this.mainBetOffer = mainBetOffer;
	}
	
	
	//Getters and Setters start from here
	public EventData getEvent() {
		return event;
	}

	public void setEvent(EventData event) {
		this.event = event;
	}

	public LiveData getLiveData() {
		return liveData;
	}

	public void setLiveData(LiveData liveData) {
		this.liveData = liveData;
	}

	public MainBetOffer getMainBetOffer() {
		return mainBetOffer;
	}

	public void setMainBetOffer(MainBetOffer mainBetOffer) {
		this.mainBetOffer = mainBetOffer;
	}
	
	
	@Override
	public String toString() {
		return "LiveEvent [event=" + event + ", liveData=" + liveData + ", mainBetOffer=" + mainBetOffer + "]";
	}
	
}
