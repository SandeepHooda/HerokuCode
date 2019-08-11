package com.phone.call;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phone.text.SMS;
import com.plivo.helper.exception.PlivoException;

/**
 * Servlet implementation class MakeACall
 */
public class MakeACall extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeACall() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String messageUrl = "https://idonotremember-app.appspot.com/CallLog?id="+request.getParameter("messageID");
			if ("sanhoo-home-security".equalsIgnoreCase(request.getParameter("source"))) {
				messageUrl = "https://sanhoo-home-security.appspot.com/CallLog?id="+request.getParameter("messageID");
			}
			Call.makeACall(request.getParameter("phone"), messageUrl,request.getParameter("fromNumber") );
		
		} catch (Exception e) {
			
			e.printStackTrace();
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "important_parameter needed");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
