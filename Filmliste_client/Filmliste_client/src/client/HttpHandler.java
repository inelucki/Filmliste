package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

public class HttpHandler {

	private static final Charset charset = Charset.forName("UTF-8");
	
	public static JSONObject sendSimpleRequest(String urlRaw, String method){
    	try {
			URL url = new URL(urlRaw);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			JSONObject obj = null;
			
			if (con != null && con.getInputStream() != null) {
				try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());){
					JSONTokener tokener = new JSONTokener(in);
					obj = new JSONObject(tokener);
				}
			}
			return obj;
		} catch (IOException e) {
			JSONObject fail = new JSONObject();
			fail.put("statusOK", false);
			fail.put("errormessage", "server nicht erreichbar oder falsche ressource");
			return fail;
		}
    }
	    
	    public static JSONObject sendRequestWithPayload(String urlRaw, String method, JSONObject payload){
	    	
	    	try {
				URL url = new URL(urlRaw);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(method);
				con.setDoOutput(true);
				con.setRequestProperty("Accept-Charset", charset.name());
				con.setRequestProperty("Content-Type", "application/json;charset=" + charset.name());
				
				if(con != null && con.getOutputStream() != null){
					try(OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), charset)){
						payload.write(out);
						out.flush();
					}
				}
				
				JSONObject obj = null;
				if (con != null && con.getInputStream() != null) {
					try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());){
						JSONTokener tokener = new JSONTokener(in);
						obj = new JSONObject(tokener);
					}
				}
				return obj;
			} catch (IOException e) {
				JSONObject fail = new JSONObject();
				fail.put("statusOK", false);
				fail.put("errormessage", "server nicht erreichbar oder falsche ressource");
				return fail;
			}
	    }
	
//public static String sendRequestWithPayload(String urlRaw, String method, String payload){
//	    	
//	    	try {
//				URL url = new URL(urlRaw);
//				HttpURLConnection con = (HttpURLConnection) url.openConnection();
//				con.setRequestMethod(method);
//				con.setDoOutput(true);
//				con.setRequestProperty("Accept-Charset", charset.name());
//				con.setRequestProperty("Content-Type", "application/json;charset=" + charset.name());
//				
//				if(con != null && con.getOutputStream() != null){
//					try(OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), charset)){
//						out.write(payload);
//						out.flush();
//					}
//				}
//				
//				StringBuffer sb = new StringBuffer();
//				if (con != null && con.getInputStream() != null) {
//					try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
//					BufferedReader bufferedReader = new BufferedReader(in)){
//						String line = null;
//						while((line=bufferedReader.readLine()) != null){
//							sb.append(line);
//							sb.append("/n");
//						}
//					}
//				}
//				return sb.toString();
//			} catch (IOException e) {
//				e.printStackTrace();
//				return "error";
//			}
//	    }
}
