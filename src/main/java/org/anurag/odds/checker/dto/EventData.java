package org.anurag.odds.checker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("sport")
	private String sport;
	
	@JsonProperty("tags")
	private List<String> tags;
	
	
	//constructors start from here
	public EventData() {}
	
	public EventData(Long id, String name, String sport, List<String> tags) {
		this.id = id;
		this.name = name;
		this.sport = sport;
		this.tags = tags;
	}

	
	//Getters and Setters start from here
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	
	@Override
	public String toString() {
		return "EventData [id=" + id + ", name=" + name + ", sport=" + sport + ", tags=" + tags + "]";
	}
	
}
