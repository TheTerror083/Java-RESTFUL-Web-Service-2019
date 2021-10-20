package RH.PE;

import java.io.IOException;

/** 
* Includes the runnable implementation for the execution thread
*/
public class Manag implements Runnable {

	private GUI software;
	@Override
	public void run() {
		try {
			software = new GUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
