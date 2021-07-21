package org.anurag.odds.checker.constants;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

public class HttpObjects {
	

	public static final HttpClient HTTP_CLIENT   =  HttpClient.newBuilder()
														      .connectTimeout(Duration.ofMinutes(1))
														      .build();

	public static final HttpRequest HTTP_GET_REQUEST = HttpRequest.newBuilder()
															  .uri(URI.create(AppConstants.KAMBI_REST_API_URL))
															  .GET()
															  .timeout(Duration.ofSeconds(10))
															  .header("accept", "application/json")
															  .build();
	

}
