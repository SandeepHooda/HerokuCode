package mangodb;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;




public class MangoDB {
	public static final String mlabKeyReminder = "oEEHExhtLS3QShn3Y2Kl4_a4nampQKj9";
	public static final String mlabKeySonu = "soblgT7uxiAE6RsBOGwI9ZuLmcCgcvh_";
	public static final String noCollection = "";

	
	public static String getDocumentWithQuery(String dbName, String collection,  String documentKey, String keyName, boolean isKeyString, String mlabApiKey, String query){
		if (null == mlabApiKey) {
			mlabApiKey = mlabKeyReminder;
		}
		String httpsURL  = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+mlabApiKey;
		if (null != documentKey){
			if (isKeyString){
				httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
			}else {
				httpsURL += "&q=%7B%22"+keyName+"%22:%22"+documentKey+"%22%7D";
			}
			
		}
		
		if (null != query ){
			httpsURL += query;
			
		}
		//System.out.println("This is the url "+httpsURL);
		String responseStr = "";
		HttpURLConnection connection = null;
		 try {
			
			 URL url = new URL(httpsURL);
	            
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	    conn.setRequestMethod("GET");
	    	    BufferedReader in = new BufferedReader(    new InputStreamReader(conn.getInputStream()));
	    		String inputLine;
	    		StringBuffer responseBuf = new StringBuffer();
	    		while ((inputLine = in.readLine()) != null) {
	    			responseBuf.append(inputLine);
	    		}
	    		in.close();
	    		
	    	
	            responseStr =responseBuf.toString();
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        	return null;
	        }finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
		
		
			responseStr = responseStr.replaceFirst("\\[", "").trim();
			 if (responseStr.indexOf("]") >= 0){
				
				 responseStr = responseStr.substring(0, responseStr.length()-1);
			 }
		
		 return responseStr;
	}
	public static String getADocument(String dbName, String collection, String documentKey,String keyName,  boolean isKeyString, String mlabApiKey){
		return getDocumentWithQuery(dbName,  collection,  documentKey,keyName, isKeyString, mlabApiKey, null);
	}
	public static String getADocument(String dbName, String collection,  String documentKey, String keyName, String mlabApiKey){
		
		return getDocumentWithQuery(dbName,  collection,  documentKey, keyName,true, mlabApiKey, null);
		
	}
	public static String getData(String db, String collection,  String apiKey ){
		if (null == apiKey) {
			apiKey = mlabKeyReminder;
		}
		db = db.toLowerCase();
		return getADocument(db,collection,null,null,apiKey);
	}
	
	
	//Create 
public static void createNewDocumentInCollection(String dbName,String collection,  String data, String key){
	if (null == key) {
		key = mlabKeyReminder;
	}
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+key;
		HttpURLConnection connection = null;
		 try {
			 //data =URLEncoder.encode(data, "UTF-8");
		        URL url = new URL(httpsURL);
		       
		        connection = (HttpURLConnection) url.openConnection();	
		        connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
		        connection.setRequestProperty("Content-Type",          "application/json");
		        connection.setRequestProperty( "charset", "utf-8");
		        connection.setRequestProperty("Content-Length",            Integer.toString(data.length()));
		        connection.setUseCaches( false );
	          // System.out.println(" data to be created "+data);
	            
		      //Send request
	           OutputStreamWriter wr = new OutputStreamWriter (
		            connection.getOutputStream());
		        wr.write(data);
		        wr.flush();
		      
		       // System.out.println(" data base update done");
		        
		        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line, response = "";

	            while ((line = reader.readLine()) != null) {
	                // Process line...
	                response += line;
	            }
	            reader.close();
	            wr.close();

	           //System.out.println(response);
	 
	        } catch (IOException e) {
	        	
	        }finally {
	        	if (connection != null) {
	                connection.disconnect();
	              }
	        }
	}
	
public static void updateData(String dbName,String collection, String data, String documentKey,  String apiKey){
	if (null == apiKey) {
		apiKey = mlabKeyReminder;
	}
	String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+apiKey;
	if (null != documentKey){
		httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
		
	}	
	
	HttpURLConnection connection = null;
	
	 try {
		
	        URL url = new URL(httpsURL);
	        connection = (HttpURLConnection) url.openConnection();	
	        connection.setDoOutput(true);
           connection.setRequestMethod("PUT");
	        connection.setRequestProperty("Content-Type",          "application/json");
	        connection.setRequestProperty( "charset", "utf-8");
	       
	        connection.setRequestProperty("Content-Length", 
	                Integer.toString(data.getBytes().length));
	        connection.setUseCaches( false );
	        System.out.println("insering data "+data);
	      //Send request
	        DataOutputStream wr = new DataOutputStream (
	            connection.getOutputStream());
	        wr.writeBytes(data);
	        wr.close();
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line, response = "";

            while ((line = reader.readLine()) != null) {
                // Process line...
                response += line;
            }
            reader.close();
           
           System.out.println(" Update response "+response);
         System.out.println("Updated the DB  collection "+collection+data);

       } catch (IOException e) {
       	// log.info("Error while  upfdating DB  collection "+collection+" Message "+e.getMessage());
       	e.printStackTrace();
       	
       }finally {
           if (connection != null) {
               connection.disconnect();
             }
           }
	
}
	
public static void deleteDocument(String dbName,String collection,  String dataKeyTobeDeleted, String key){
	System.out.println(" deleting "+dataKeyTobeDeleted);
	if (null == key) {
		key = mlabKeyReminder;
	}
		
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"/"+dataKeyTobeDeleted+"?apiKey="+key;
		 HttpURLConnection connection = null;
		
		 try {
			
		        URL url = new URL(httpsURL);
		        connection = (HttpURLConnection) url.openConnection();	
		        
	            connection.setRequestMethod("DELETE");
		       
	            int responseCode = connection.getResponseCode();
		      
				System.out.println(" deleted success  "+dataKeyTobeDeleted+" responseCode "+responseCode);
	            
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	

}
