package mangodb;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;





public class MangoDB {
	public static final String mlabKeySonu = "soblgT7uxiAE6RsBOGwI9ZuLmcCgcvh_";
	public static final String mlabKeyReminder = "oEEHExhtLS3QShn3Y2Kl4_a4nampQKj9"; 
	public static final String noCollection = "";

	
	
	public static String getDocumentWithQuery(String dbName, String collection,  String documentKey, boolean isKeyString, String mlabApiKey, String query){
		if (null == mlabApiKey) {
			mlabApiKey = mlabKeyReminder;
		}
		String httpsURL  = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"?apiKey="+mlabApiKey;
		if (null != documentKey){
			if (isKeyString){
				httpsURL += "&q=%7B%22_id%22:%22"+documentKey+"%22%7D";
			}else {
				httpsURL += "&q=%7B%22_id%22:"+documentKey+"%7D";
			}
			
		}
		
		if (null != query ){
			httpsURL +=  query;
		}
		//System.out.println("This is the url "+httpsURL);
		String responseStr = "";
		HttpURLConnection connection = null;
		 try {
			
		        URL url = new URL(httpsURL);
		        connection = (HttpURLConnection) url.openConnection();	
		        connection.setRequestMethod("GET");

		        BufferedReader in = new BufferedReader(
				        new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		      
		
	            responseStr =response.toString();
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	//log.warning("Error while gettiung data dbName: "+dbName+" collection :"+collection+" documentKey: "+documentKey+e.getLocalizedMessage());
	        	return null;
	        } finally {
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
	public static String getADocument(String dbName, String collection, String documentKey, boolean isKeyString, String mlabApiKey){
		return getDocumentWithQuery(dbName,  collection,  documentKey, isKeyString, mlabApiKey, null);
	}
	public static String getADocument(String dbName, String collection,  String documentKey,String mlabApiKey){
		
		return getDocumentWithQuery(dbName,  collection,  documentKey, true, mlabApiKey, null);
		
	}
	public static String getData(String db, String collection,  String apiKey ){
		db = db.toLowerCase();
		return getADocument(db,collection,null,apiKey);
	}
	
	
public static void deleteDocument(String dbName,String collection,  String dataKeyTobeDeleted, String key){
		
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collection+"/"+dataKeyTobeDeleted+"?apiKey="+key;
		 HttpURLConnection connection = null;
		 try {
			
		        URL url = new URL(httpsURL);
		        connection = (HttpURLConnection) url.openConnection();	
		        
	            connection.setRequestMethod("DELETE");
		       
		        
	            BufferedReader in = new BufferedReader(
				        new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		      
		
	            
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	
public static void createNewDocument(String dbName,String collectionToCreate,  String data, String key){
		
		String httpsURL = "https://api.mlab.com/api/1/databases/"+dbName+"/collections/"+collectionToCreate+"?apiKey="+key;
		 HttpURLConnection connection = null;
		 try {
			
		        URL url = new URL(httpsURL);
		        connection = (HttpURLConnection) url.openConnection();	
		        connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
		        connection.setRequestProperty("Content-Type",          "application/json");
		        
		        connection.setRequestProperty("Content-Length",            Integer.toString(data.getBytes().length));
	           System.out.println(" data to be created "+data);
	            
		      //Send request
		        DataOutputStream wr = new DataOutputStream (
		            connection.getOutputStream());
		        wr.writeBytes(data);
		        wr.close();
		        System.out.println(" done");
		        
	            
	 
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
	}
	
	public static void insertOrUpdateData(String dbName,String collection, String data,  String apiKey, String documentKey){
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
		        connection.setRequestProperty("Content-Length", 
		                Integer.toString(data.getBytes().length));
		        System.out.println("insering data "+data);
		      //Send request
		        DataOutputStream wr = new DataOutputStream (
		            connection.getOutputStream());
		        wr.writeBytes(data);
		        wr.close();
	            
	           //log.info("Updated the DB  collection "+collection+data);
	 
	        } catch (IOException e) {
	        	// log.info("Error while  upfdating DB  collection "+collection+" Message "+e.getMessage());
	        	e.printStackTrace();
	        	
	        }finally {
	            if (connection != null) {
	                connection.disconnect();
	              }
	            }
		
	}
	

}
