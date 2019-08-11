package thread;

public class Worker implements Runnable {

	public void run() {
		for (int i=0 ;i<10; i++) {
			System.out.println(" I am doing your work ");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}

}
