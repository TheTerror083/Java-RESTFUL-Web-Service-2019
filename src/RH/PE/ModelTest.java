package RH.PE;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ModelTest {

	@Test
	void test() {
		
		Model testM = new Model();
		assertNotEquals(testM, null);
		
		ArrayList<Food> testFood = testM.getFoodList();
		
		Model testM2 = new Model();
		ArrayList<Food> testFood2 = testM2.getFoodList();
		assertEquals(testFood2.size(),testFood.size());
		
		int i;
		for (i=0; i < testFood.size(); i++)
		{
			assertEquals(testFood.get(i).getCalories(), testFood2.get(i).getCalories());
			assertEquals(testFood.get(i).getName(), testFood2.get(i).getName());
			assertEquals(testFood.get(i).getDescr(), testFood2.get(i).getDescr());
			assertEquals(testFood.get(i).getPrice(), testFood2.get(i).getPrice());
		}
		
		String str1 = testM.toString();
		assertNotEquals(str1, null);
		
		String str2 = testM.toString();
		assertEquals(str1, str2);
		
		//remove all food from the list
		testM.removeFood(testM.getFoodList().size());
		assertEquals(testM.getFoodList().size(), 0);
	}

}
