import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
import org.junit.Test;

public class EmpDataBaseTest {
	//@RepeatedTest(10)
	public void testCreateDataBase() {
		EmpDataBase dataBase = new EmpDataBase();
		
		assertTrue(dataBase.checkUser("Bob" , "Smith") != false);
	}
	
	//@Test
	public void testSendMsg() throws IOException {
		EmpDataBase dataBase = new EmpDataBase();
		Message dummyMsg = new Message("Dummy");
		assertTrue(dataBase.loginEmployee(2, "Tacos") == true);
	}
	
}