package org.anurag.odds.checker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Utility class to provide various Reusable conversion methods
 *
 */
public class ConversionUtils {

	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	
	/**
	 * It will convert a Date into a String with nano seconds and 'T' char removed
	 * @param date : input date
	 * @return Date string without nano-second and 'T' char
	 */
	public static String getDateWithoutNanoSec(LocalDateTime date) {

		//Converting  "2021-05-28T15:34:58.094980"  to 
		//			  "2021-05-28 15:34:58"
		return DATETIME_FORMATTER.format(date);
	}
	
	
	
	/**
	 * Generic Type safe method to convert JSON into Object of desired type
	 * @param json : input JSON string
	 * @param type : Java POJO
	 * @return Object of type T
	 * @throws JsonMappingException 
	 * @throws JsonProcessingException
	 */
    public static <T>T covertJsonStringToObject(String json, Class<T> type) 
    		throws JsonMappingException, JsonProcessingException {
        
    	//Convert Json into object of specific Type
        return JSON_MAPPER.readValue(json, type);
    }
    
    
    
    /**
     * In a BigDecimal value, it will round off decimal values to 2 decimal places
	 * @param bigDecimal : input decimal value
	 * @return BigDecimal with a scale of 2 and RoundingMode.HALF_EVEN
	 */
	public static BigDecimal roundTwoDecimals(BigDecimal bigDecimal) {
		
		if(bigDecimal != null) {
			
			//RoundingMode.HALF_EVEN
			//rounding mode that statistically minimizes cumulative error when applied repeatedly over a sequence of calculations.
			//It is sometimes known as "Banker's rounding" and is chiefly used in the USA. 
			bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
		}
		return bigDecimal;
	}

}
