package org.anurag.odds.checker.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
class AppInputValidatorTest {
	
	@InjectMocks
	private AppInputValidator underTest;
	
	@BeforeAll
	void beforeAll() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("For a valid input from the command line, event Id should be returned")
	void testValidateAndGet() {
		
		//pass one argument
		String[] input = {"12345"};
		Long eventId = underTest.validateAndGet(input);
		assertEquals(12345, eventId);
	}

	@Test
	@DisplayName("For no input from the command line, Exception should be thrown")
	void testValidateAndGet2() {
		
		//pass no argument
		String[] input = {};
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
				() -> underTest.validateAndGet(input));
		assertEquals("Please provide an Event Id as input", exception.getMessage());
	}
	
	
	@Test
	@DisplayName("For more than 1 input from the command line, Exception should be thrown")
	void testValidateAndGet3() {
		
		//pass 2 arguments
		String[] input = {"123","124"};
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
				() -> underTest.validateAndGet(input));
		assertEquals("Please provide an Event Id as input", exception.getMessage());
	}
	
	@Test
	@DisplayName("Any input value other than Numeric should throw an Exception")
	void testValidateAndGet4() {
		
		//pass 1 argument
		String[] input = {"ABCD"};
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
				() -> underTest.validateAndGet(input));
		assertEquals("Please provide a valid Event Id with Numeric characters only", exception.getMessage());
	}

}
