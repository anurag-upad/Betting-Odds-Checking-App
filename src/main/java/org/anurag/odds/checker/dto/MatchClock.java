package org.anurag.odds.checker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchClock {
	
	@JsonProperty("running")
	private boolean running;
	
	@JsonProperty("disabled")
	private boolean disabled;
	
	@JsonProperty("period")
	private String period;
	
	@JsonProperty("minutesLeftInPeriod")
	private int minutesLeftInPeriod;
	
	@JsonProperty("secondsLeftInMinute")
	private int secondsLeftInMinute;
	
	
	//constructors start from here
	public MatchClock() {}
	
	public MatchClock(boolean running, boolean disabled, String period, int minutesLeftInPeriod, int secondsLeftInMinute) {
		this.running = running;
		this.disabled = disabled;
		this.period = period;
		this.minutesLeftInPeriod = minutesLeftInPeriod;
		this.secondsLeftInMinute = secondsLeftInMinute;
	}


	//Getters and Setters start from here
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getMinutesLeftInPeriod() {
		return minutesLeftInPeriod;
	}

	public void setMinutesLeftInPeriod(int minutesLeftInPeriod) {
		this.minutesLeftInPeriod = minutesLeftInPeriod;
	}

	public int getSecondsLeftInMinute() {
		return secondsLeftInMinute;
	}

	public void setSecondsLeftInMinute(int secondsLeftInMinute) {
		this.secondsLeftInMinute = secondsLeftInMinute;
	}
	
	
	@Override
	public String toString() {
		return "MatchClock [running=" + running + ", disabled=" + disabled + ", minutesLeftInPeriod="
				+ minutesLeftInPeriod + ", secondsLeftInMinute=" + secondsLeftInMinute + "]";
	}

}
