package RH.PE;

import java.io.IOException;

/** 
* Includes the main function and the execution thread
*/
public class Exec{
	
	public static void main(String[] args) throws IOException{
		
		Thread execute = new Thread(new Manag(), "executeThread");
		execute.start();
	}
}