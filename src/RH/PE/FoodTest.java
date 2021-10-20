package RH.PE;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FoodTest {

	@Test
	void test() {
		Food testf = new Food();
		
		//testing the calories
		testf.setCalories("8");
		assertEquals(testf.getCalories(),"8");
		
		//testing the name
		testf.setName("TestFood");
		assertEquals(testf.getName(),"TestFood");
		
		//testing the price
		testf.setPrice("$10");
		assertEquals(testf.getPrice(),"$10");
		
		//testing the description
		testf.setDescr("Test Food Object");
		assertEquals(testf.getDescr(),"Test Food Object");
	}

}
