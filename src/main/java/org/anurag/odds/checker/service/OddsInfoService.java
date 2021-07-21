package org.anurag.odds.checker.service;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.anurag.odds.checker.dto.ConsoleOutput;
import org.anurag.odds.checker.dto.KambiApiResponse;
import org.anurag.odds.checker.dto.LiveEvent;
import org.anurag.odds.checker.exception.OddsCheckerAppException;


public interface OddsInfoService {
	
	
	KambiApiResponse getOddsInfoByKambiApiUrl(HttpClient httpClient, HttpRequest httpRequest) throws OddsCheckerAppException;
	
	LiveEvent filterResponseByEventIdandTag(KambiApiResponse apiResponse, Long eventId) throws OddsCheckerAppException;
	
	void getPollIntervalByMatchStatus(LiveEvent filteredEvent, AtomicInteger oldPollingInterval);

	String compareAndDisplayOddsChanges(ConsoleOutput consoleOutputObj, Map<String,BigDecimal> previousOdds);
	
	
}
