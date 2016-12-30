package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpHandler {

	private static final Charset charset = Charset.forName("UTF-8");
	private static final String HOST = "http://localhost:8080/";
	
	public static String sendSimpleRequest(String res, String method){
	    	
	    	try {
				URL url = new URL(HOST+res);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(method);
				StringBuffer sb = new StringBuffer();
				
				if (con != null && con.getInputStream() != null) {
					try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in)){
						String line = null;
						while((line=bufferedReader.readLine()) != null){
							sb.append(line);
							sb.append("/n");
						}
					}
				}
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
	    }
	    
	    public static String sendRequestWithPayload(String res, String method, String payload){
	    	
	    	try {
				URL url = new URL(HOST+res);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod(method);
				con.setDoOutput(true);
				con.setRequestProperty("Accept-Charset", charset.name());
				con.setRequestProperty("Content-Type", "application/json;charset=" + charset.name());
				
				if(con != null && con.getOutputStream() != null){
					try(OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), charset)){
						out.write(payload);
						out.flush();
					}
				}
				
				StringBuffer sb = new StringBuffer();
				if (con != null && con.getInputStream() != null) {
					try(InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in)){
						String line = null;
						while((line=bufferedReader.readLine()) != null){
							sb.append(line);
							sb.append("/n");
						}
					}
				}
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
	    }
	
}
