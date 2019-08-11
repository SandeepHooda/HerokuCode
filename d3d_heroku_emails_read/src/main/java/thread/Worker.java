package thread;

public class Worker implements Runnable {

	public void run() {
		int minutes = 5;
		
		for (int i=0 ;i<6 * minutes; i++) {
			System.out.println(" I am checking your emails ");
			ReceiveEmailWithAttachment.main(null);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

}
