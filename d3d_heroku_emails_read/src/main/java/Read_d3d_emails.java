import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import thread.Worker;


/**
 * Servlet implementation class Welcome
 */

public class Read_d3d_emails extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Read_d3d_emails() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Thread t = new Thread(new Worker());
    	t.setDaemon(false);
    	t.start();
    	System.out.println(" I have asked the worker to do your work");
    }
	

	

}
