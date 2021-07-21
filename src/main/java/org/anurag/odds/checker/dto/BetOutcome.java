package org.anurag.odds.checker.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BetOutcome {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("label")
	private String label;
	
	@JsonProperty("odds")
	private BigDecimal odds;
	
	
	//constructors start from here
	public BetOutcome() {}
	
	public BetOutcome(Long id, String label, BigDecimal odds) {
		this.id = id;
		this.label = label;
		this.odds = odds;
	}

	
	//Getters and Setters start from here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getOdds() {
		return odds;
	}

	public void setOdds(BigDecimal odds) {
		this.odds = odds;
	}
	
	
	@Override
	public String toString() {
		return "BetOutcome [id=" + id + ", label=" + label + ", odds=" + odds + "]";
	}
	
}
