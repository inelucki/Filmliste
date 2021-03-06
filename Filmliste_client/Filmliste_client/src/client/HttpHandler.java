package client;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.scene.image.Image;

public class HttpHandler {

	private static final Charset charset = Charset.forName("UTF-8");
	private static final Base64.Encoder base64 = Base64.getEncoder();
	private static final String user = "ine";
	private static final String pw = "pw";
	
	// for tests on localhost only
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){

	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("localhost")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}

	
	public static JSONObject sendSimpleRequest(String urlRaw, String method){
    	try {
			URL url = new URL(urlRaw);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty("Authorization", "Basic "+base64.encodeToString(new String(user+":"+pw).getBytes()));
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
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setRequestProperty("Authorization", "Basic "+base64.encodeToString(new String(user+":"+pw).getBytes()));
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
	    
    public static JSONObject sendPicture(String url, File pic){
    	try{
	    	String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
	    	String CRLF = "\r\n"; // Line separator required by multipart/form-data.
	
	    	HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
	    	connection.setDoOutput(true);
	    	connection.setRequestProperty("Authorization", "Basic "+base64.encodeToString(new String(user+":"+pw).getBytes()));
	    	connection.setRequestMethod("PUT");
	    	connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	
	    	try (
	    	    OutputStream output = connection.getOutputStream();
	    	    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
	    	) {
	    	    // Send image file.
	    	    writer.append("--" + boundary).append(CRLF);
	    	    writer.append("Content-Disposition: form-data; name=\"picture\"; filename=\"" + pic.getName() + "\"").append(CRLF);
	    	    writer.append("Content-Type: image/gif").append(CRLF); // Text file itself must be saved in this charset!
	    	    writer.append(CRLF).flush();
	    	    Files.copy(pic.toPath(), output);
	    	    output.flush(); // Important before continuing with writer!
	    	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
	
	    	    // End of multipart/form-data.
	    	    writer.append("--" + boundary + "--").append(CRLF).flush();
	    	}
	
	    	JSONObject obj = null;
			if (connection != null && connection.getInputStream() != null) {
				try(InputStreamReader in = new InputStreamReader(connection.getInputStream(), Charset.defaultCharset());){
					JSONTokener tokener = new JSONTokener(in);
					obj = new JSONObject(tokener);
				}
			}
			return obj;
    	}
    	catch(IOException e){
    		JSONObject fail = new JSONObject();
			fail.put("statusOK", false);
			fail.put("errormessage", "server nicht erreichbar oder falsche ressource");
			return fail;
    	}
    }
    
    public static Image retrievePicture(String urlRaw){
    	try {
			URL url = new URL(urlRaw);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "Basic "+base64.encodeToString(new String(user+":"+pw).getBytes()));
			Image im = new Image(con.getInputStream());
			return im;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    }
}
