

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mangodb.MangoDB;

/**
 * Servlet implementation class ImageTrigger
 */
public class ImageTrigger extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageTrigger() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println(MangoDB.getData("sandeepdb", "gate-keeper", MangoDB.mlabKeySonu));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MangoDB.createNewDocument("sandeepdb", "gate-keeper", "{\"_id\":\""+(new Date().getTime())+"\"}", MangoDB.mlabKeySonu);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data = "["+MangoDB.getData("sandeepdb", "gate-keeper", MangoDB.mlabKeySonu)+"]";
		 JSONParser parser = new JSONParser();
		 try {
			Object obj = parser.parse(data);
			 JSONArray  jsonObject = ( JSONArray ) obj;
			 Iterator<JSONObject > iterator = jsonObject.iterator();
	            while (iterator.hasNext()) {
	            	JSONObject  aObject = (JSONObject )iterator.next();
	           
	               
	                MangoDB.deleteDocument("sandeepdb", "gate-keeper", (String)aObject.get("_id"), MangoDB.mlabKeySonu);
	            }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
